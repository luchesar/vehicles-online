package utils.helpers

import app.ConfigProperties._
import org.apache.commons.codec.binary.Hex
import java.security.SecureRandom
import play.api.mvc.{RequestHeader, Cookie, SimpleResult, Request}
import Config.cookieMaxAge

object CryptoHelper {

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

  private def newSessionSecretyKey = if (encryptCookies) Hex.encodeHexString(CryptoHelper.getSecureRandomBytes(16)) else ""

  def getSessionSecretKeyFromRequest(request: RequestHeader)(implicit encryption: CookieEncryption, cookieNameHashing: CookieNameHashing): Option[String] =
      request.cookies.get(sessionSecretKeyCookieName).map { cookie =>
        encryption.decrypt(cookie.value)
      }

  def ensureSessionSecretKeyInResult(result: SimpleResult)(implicit request: Request[_], encryption: CookieEncryption,
                                                           cookieNameHashing: CookieNameHashing): (SimpleResult, String) =
    CryptoHelper.getSessionSecretKeyFromRequest(request) match {
      case Some(saltFromRequest) =>
        (result, saltFromRequest)
      case None =>
        val newSalt = CryptoHelper.newSessionSecretyKey
        if (newSalt.isEmpty)
          (result, newSalt)
        else {
          val newSaltCookie = createCookie(name = sessionSecretKeyCookieName,
            value = encryption.encrypt(newSalt))
          val resultWithSalt = result.withCookies(newSaltCookie)
          (resultWithSalt, newSalt)
        }
    }

  def createCookie(name: String, value: String) = Cookie(name = name,
    value = value,
    maxAge = Some(cookieMaxAge)/*,
    secure = true*/
  )
}
