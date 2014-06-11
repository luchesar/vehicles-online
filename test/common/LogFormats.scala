package common

import org.scalatest.{Matchers, WordSpec}

class LogFormatsUnitSpec extends WordSpec with Matchers{

  "Anonymize" should {
    "empty string should return nothing" in {
      val inputString = ""
      LogFormats.anonymize(inputString) should equal("")
    }

    "string of greater than 8 characters should return 4 characters and the rest stars" in {
      val inputString = "qwertyuiop"
      LogFormats.anonymize(inputString) should equal("******uiop")
    }

    "string of less than 8 characters should return half characters and the rest stars" in {
      val inputString = "qwer"
      LogFormats.anonymize(inputString) should equal("**er")
    }

    "string with an odd number of characters should return more than half stars and the remainder characters" in {
      val inputString = "qwert"
      LogFormats.anonymize(inputString) should equal("***rt")
    }
  }
}
