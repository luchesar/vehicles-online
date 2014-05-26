package common

import org.scalatest.{Matchers, WordSpec}
import play.api.test.{FakeApplication, WithApplication}
import utils.helpers._

class EncryptedClientSideSessionSpec extends WordSpec with Matchers {
  val sessionSecretKey = "sessionSecret"
  val cookieName = "cookieName"
  implicit val noCookieFlags = new NoCookieFlags
  implicit val noEncryption = new NoEncryption with CookieEncryption
  implicit val noHashing = new NoHash with CookieNameHashing
  implicit val sha1Hashing = new Sha1Hash with CookieNameHashing

  "EncryptedClientSideSession" should {
    "return a new CookieName type consisting of the session secret key plus the cookie name that we can see in clear text when hashing is not used" in new WithApplication {
      implicit val aesEncryption = new AesEncryption with CookieEncryption
      val encryptedClientSideSession = new EncryptedClientSideSession(sessionSecretKey)(noCookieFlags, noEncryption, noHashing)
      val encryptedCookieName = encryptedClientSideSession.nameCookie(cookieName)
      encryptedCookieName.value should equal(s"$sessionSecretKey$cookieName")
    }

    "return a deterministic hashed cookie name (the hash will always be the same value for the same inputs)" in new WithApplication {
      implicit val aesEncryption = new AesEncryption with CookieEncryption
      val encryptedClientSideSession = new EncryptedClientSideSession(sessionSecretKey)(noCookieFlags, noEncryption, sha1Hashing)
      val encryptedCookieName1 = encryptedClientSideSession.nameCookie(cookieName)
      val encryptedCookieName2 = encryptedClientSideSession.nameCookie(cookieName)
      encryptedCookieName1.value should equal(encryptedCookieName2.value)
    }

    "return a cookie whose value is prefixed with the cookie name that we can see in clear text when hashing is not used" in new WithApplication {
      val encryptedClientSideSession = new EncryptedClientSideSession(sessionSecretKey)(noCookieFlags, noEncryption, noHashing)
      val cookieNameType = encryptedClientSideSession.nameCookie(cookieName)
      val value = "value"
      val cookie = encryptedClientSideSession.newCookie(cookieNameType, value)
      cookie.value should equal(cookie.name + value)
    }

    "allow the client to extract the encrypted value from the cookie" in new WithApplication(app = fakeAppWithConfig) {
      implicit val aesEncryption = new AesEncryption with CookieEncryption
      val encryptedClientSideSession = new EncryptedClientSideSession(sessionSecretKey)(noCookieFlags, aesEncryption, sha1Hashing)
      val cookieNameType = encryptedClientSideSession.nameCookie(cookieName)
      val value = "value"
      val cookie = encryptedClientSideSession.newCookie(cookieNameType, value)
      val valueFromCookie = encryptedClientSideSession.getCookieValue(cookie)
      valueFromCookie should equal(value)
    }
  }

  private val fakeAppWithConfig = FakeApplication(
    additionalConfiguration = Map("application.secret256Bit" -> "MnPSvGpiEF5OJRG3xLAnsfmdMTLr6wpmJmZLv2RB9Vo="))
}
