package utils.helpers

import org.scalatest.{Matchers, WordSpec}
import play.api.test.WithApplication

final class HashSpec extends WordSpec with Matchers {
  "encryptCookieName" should {
    "return an encrypted string" in new WithApplication {
      val sha1Hash = new Sha1Hash
      sha1Hash.hash(ClearText) should not equal ClearText
    }

    "returns the same hash repeatedly" in new WithApplication {
      val sha1Hash = new Sha1Hash
      val first = sha1Hash.hash(ClearText)
      val second = sha1Hash.hash(ClearText)
      first should equal(second)
    }
  }

  private final val ClearText = "qwerty"
}
