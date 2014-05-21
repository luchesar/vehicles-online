package utils.helpers

import org.scalatest.{Matchers, WordSpec}
import play.api.test.WithApplication

final class NoEncryptionSpec extends WordSpec with Matchers {
  "encryptCookie" should {
    "return an clear text string" in new WithApplication {
      val noEncryption = new NoEncryption
      noEncryption.encrypt(clearText) should equal(clearText)
    }
  }

  "decryptCookie" should {
    "return a the same input string" in new WithApplication {
      val noEncryption = new NoEncryption
      noEncryption.decrypt(clearText) should equal(clearText)
    }
  }

  private final val clearText = "qwerty"
}
