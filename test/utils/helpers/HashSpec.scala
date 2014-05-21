package utils.helpers

import org.scalatest.{Matchers, WordSpec}
import play.api.test.WithApplication

final class HashSpec extends WordSpec with Matchers {
  "encryptCookieName" should {
    "return an encrypted string" in new WithApplication {
      val sha1Hash = new Sha1Hash
      sha1Hash.hash(clearText) should not equal clearText
    }

    "returns the same hash repeatedly" in new WithApplication {
      val sha1Hash = new Sha1Hash
      val first = sha1Hash.hash(clearText)
      val second = sha1Hash.hash(clearText)
      first should equal(second)
    }
  }

  private val clearText = "qwerty"
}
