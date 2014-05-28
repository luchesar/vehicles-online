package utils.helpers

import org.scalatest.{Matchers, WordSpec}
import helpers.WithApplication

final class Sha1HashSpec extends WordSpec with Matchers {

  "Sha1Hash" should {
    "return a hashed string" in new WithApplication {
      sha1Hash.hash(ClearText) should not equal ClearText
    }

    "returns the same hash repeatedly" in new WithApplication {
      val first = sha1Hash.hash(ClearText)
      val second = sha1Hash.hash(ClearText)
      first should equal(second)
    }

    "return expected length for the digest" in new WithApplication {
      sha1Hash.digestStringLength should equal(40)
    }
  }

  private val sha1Hash = new Sha1Hash // Sharing immutable fixture objects via instance variables
  private final val ClearText = "qwerty"
}
