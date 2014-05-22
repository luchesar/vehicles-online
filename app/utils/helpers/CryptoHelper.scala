package utils.helpers

import app.ConfigProperties._
import org.apache.commons.codec.binary.Hex
import java.security.SecureRandom
import play.api.mvc.{RequestHeader, Request}
import Config.cookieMaxAge
import play.api.Logger
import mappings.disposal_of_vehicle.RelatedCacheKeys
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.Results._
import play.api.mvc.Cookie
import scala.Some
import play.api.mvc.SimpleResult
import controllers.disposal_of_vehicle.routes
import common.EncryptedCookieImplicits.SimpleResultAdapter
import ExecutionContext.Implicits.global

object CryptoHelper {

  final val CookieNameFromPayloadSize = 40 // Sha1 hash produces 160 bit (20 byte) hash value. As a hex number is 40 digits long
  private val encryptCookies = getProperty("encryptCookies", default = true)

  private def sessionSecretKeyCookieName(implicit cookieNameHashing: CookieNameHashing): String = {
    cookieNameHashing.hash("FE291934-66BD-4500-B27F-517C7D77F26B")
  }

  private def getSecureRandomBytes(numberOfBytes: Int): Array[Byte] = {
    val random = new SecureRandom()
    val bytes = new Array[Byte](numberOfBytes)
    random.nextBytes(bytes)
    bytes
  }

  private def newSessionSecretKey = if (encryptCookies) Hex.encodeHexString(CryptoHelper.getSecureRandomBytes(16)) else ""

  def getSessionSecretKeyFromRequest(request: RequestHeader)(implicit encryption: CookieEncryption, cookieNameHashing: CookieNameHashing): Option[String] =
    request.cookies.get(sessionSecretKeyCookieName).map { cookie =>
      val decrypted = encryption.decrypt(cookie.value)
      if (encryptCookies) {
        val cookieNameFromPayload = decrypted.substring(0, CryptoHelper.CookieNameFromPayloadSize)
        assert(cookieNameFromPayload == sessionSecretKeyCookieName, "The cookie name bytes from the payload must match the cookie name")
        decrypted.substring(CryptoHelper.CookieNameFromPayloadSize)
      }
      else
        decrypted
    }

  def ensureSessionSecretKeyInResult(result: SimpleResult)(implicit request: Request[_], encryption: CookieEncryption,
                                                           cookieNameHashing: CookieNameHashing): (SimpleResult, String) =
    CryptoHelper.getSessionSecretKeyFromRequest(request) match {
      case Some(sessionSecretKeyFromRequest) =>
        (result, sessionSecretKeyFromRequest)
      case None =>
        val newSessionSecretKey = CryptoHelper.newSessionSecretKey
        if (newSessionSecretKey.isEmpty)
          (result, newSessionSecretKey)
        else {
          val newSessionSecretKeyCookie = createCookie(name = sessionSecretKeyCookieName,
            value = encryption.encrypt(sessionSecretKeyCookieName + newSessionSecretKey))
          val resultWithSessionSecretKeyCookie = result.withCookies(newSessionSecretKeyCookie)
          (resultWithSessionSecretKeyCookie, newSessionSecretKey)
        }
    }

  def createCookie(name: String, value: String) = Cookie(name = name,
    value = value,
    maxAge = Some(cookieMaxAge)/*,
    secure = true*/
  )

  def handleApplicationSecretChange(implicit request: RequestHeader): Future[SimpleResult] = discardAllCookies

  def discardAllCookies(implicit request: RequestHeader): Future[SimpleResult] = {
    Logger.warn("Handling BadPaddingException or IllegalBlockSizeException by removing all cookies except seen cookie. " +
      "Has the application secret changed or has a user tampered with his session secret ?")
    val discardingCookiesKeys = request.cookies.map(_.name).filter(_ != RelatedCacheKeys.SeenCookieMessageKey).toSet
    Future(Redirect(routes.BeforeYouStart.present()).
      discardingEncryptedCookies(discardingCookiesKeys, request))
  }
}
