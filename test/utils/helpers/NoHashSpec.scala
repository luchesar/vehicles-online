package utils.helpers

import org.scalatest.{Matchers, WordSpec}
import play.api.test.WithApplication

final class NoHashSpec extends WordSpec with Matchers {

  "hash" should {
    "return a clear text string" in new WithApplication {
      val clearText = "qwerty"
      val noHash = new NoHash
      noHash.hash(clearText) should equal(clearText)
    }
  }

}
