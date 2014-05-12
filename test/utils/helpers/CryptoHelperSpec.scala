package utils.helpers

import org.scalatest.{Matchers, WordSpec}
import play.api.test.WithApplication

class CryptoHelperSpec extends WordSpec with Matchers {
  val clearText = "qwerty"

  "encryptCookie" should {
    "return an encoded string" in new WithApplication {
      CryptoHelper.encryptCookie(clearText, encryptCookies = true) should not equal clearText
    }
  }

  "decryptCookie" should {
    "return a decrypted string" in new WithApplication {
      val encrypted = CryptoHelper.encryptCookie(clearText, encryptCookies = true)
      CryptoHelper.decryptCookie(encrypted, decryptCookies = true) should equal(clearText)
    }
  }

  "encryptCookieName" should {
    "return an encoded string" in new WithApplication {
      CryptoHelper.encryptCookieName(clearText, encryptCookies = true) should not equal clearText
    }
  }

  "encryptAES" should {
    "return an encoded string" in new WithApplication {
      CryptoHelper.encryptAES(clearText, encryptFields = true) should not equal clearText
    }
  }

  "decryptAES" should {
    "return a decrypted string" in new WithApplication {
      val encrypted = CryptoHelper.encryptCookie(clearText, encryptCookies = true)
      CryptoHelper.decryptAES(encrypted, decryptFields = true) should equal(clearText)
    }
  }
}
