package utils.helpers

import org.scalatest.{Matchers, WordSpec}
import play.api.test.WithApplication

class CryptoHelperSpec extends WordSpec with Matchers {
  val clearText = "qwerty"

  "encryptAES" should {
    "return an encrypted string" in new WithApplication {
      CryptoHelper.encryptAES(clearText, encryptFields = true) should not equal clearText
    }

    "return a different encrypted string given same clear text input" in new WithApplication {
      val cipherText1 = CryptoHelper.encryptAES(clearText, encryptFields = true)
      val cipherText2 = CryptoHelper.encryptAES(clearText, encryptFields = true)

      withClue("Initialization vectors must be used to ensure output is always random") {
        cipherText1 should not equal cipherText2
      }
    }
  }

  "decryptAES" should {
    "return a decrypted string" in new WithApplication {
      val encrypted = CryptoHelper.encryptAES(clearText, encryptFields = true)
      CryptoHelper.decryptAES(encrypted, decryptFields = true) should equal(clearText)
    }
  }
}
