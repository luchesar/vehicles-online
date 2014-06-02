package common

import app.ConfigProperties._
import utils.helpers.{CookieEncryption, CookieNameHashing}
import org.apache.commons.codec.binary.Hex
import java.security.SecureRandom
import com.google.inject.Inject
import play.api.mvc.Cookie
import play.api.mvc.SimpleResult

class EncryptedClientSideSessionFactory @Inject()()(implicit cookieFlags: CookieFlags,
                                                    encryption: CookieEncryption,
                                                    cookieNameHashing: CookieNameHashing) extends ClientSideSessionFactory {

  /**
   * Session secret key must not expire before any other cookie that relies on it.
   */
  private final val SessionSecretKeyLifetime = None

  val secureCookies: Boolean = getProperty("secureCookies", default = true)

  override protected def newSession(result: SimpleResult): (SimpleResult, ClientSideSession) = {
    val sessionSecretKey = newSessionSecretKey


    val sessionSecretKeyPrefixCookieName = ClientSideSessionFactory.SessionIdCookieName
    val sessionSecretKeyCipherText = encryption.encrypt(sessionSecretKeyCookieName + sessionSecretKey)
    val (prefixValue, suffixValue) = sessionSecretKeyCipherText.splitAt(20)

    val sessionSecretKeyPrefixCookie = Cookie(
      name = sessionSecretKeyPrefixCookieName,
      value = prefixValue,
      secure = secureCookies,
      maxAge = SessionSecretKeyLifetime)

    val sessionSecretKeySuffixCookie = Cookie(
      name = sessionSecretKeyCookieName,
      value = suffixValue,
      secure = secureCookies,
      maxAge = SessionSecretKeyLifetime)

    val clientSideSession = new EncryptedClientSideSession(prefixValue, sessionSecretKey)
    val resultWithSessionSecretKeyCookie = result.withCookies(sessionSecretKeyPrefixCookie, sessionSecretKeySuffixCookie)

    (resultWithSessionSecretKeyCookie, clientSideSession)
  }

  override def getSession(request: Traversable[Cookie]): Option[ClientSideSession] = {
    val cookieName = sessionSecretKeyCookieName

    for {
      cookie <- request.find(_.name == cookieName)
      trackingId <- request.find(_.name == ClientSideSessionFactory.SessionIdCookieName)
    } yield {
      val decrypted = encryption.decrypt(trackingId.value + cookie.value)
      val (cookieNameFromPayload, sessionSecretKey) = decrypted.splitAt(cookieName.length)
      assert(cookieName == cookieNameFromPayload, "The cookie name bytes from the payload must match the cookie name")

      new EncryptedClientSideSession(trackingId.value, sessionSecretKey)
    }
  }

  private def sessionSecretKeyCookieName(implicit cookieNameHashing: CookieNameHashing): String =
    cookieNameHashing.hash("FE291934-66BD-4500-B27F-517C7D77F26B")

  private def newSessionSecretKey = Hex.encodeHexString(getSecureRandomBytes(16))

  private def getSecureRandomBytes(numberOfBytes: Int): Array[Byte] = {
    val random = new SecureRandom()
    val bytes = new Array[Byte](numberOfBytes)
    random.nextBytes(bytes)
    bytes
  }
}
