package utils.helpers

import org.scalatest.{Matchers, WordSpec}
import play.api.test.WithApplication

final class NoHashSpec extends WordSpec with Matchers {
  val noHash = new NoHash // Sharing immutable fixture objects via instance variables

  "NoHash" should {
    "return a clear text string" in new WithApplication {
      noHash.hash(ClearText) should equal(ClearText)
    }

    "return expected length for the digest" in new WithApplication {
      noHash.digestStringLength should equal(0)
    }
  }

  private final val ClearText = "qwerty"
}
