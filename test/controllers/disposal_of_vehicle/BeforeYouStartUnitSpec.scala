package controllers.disposal_of_vehicle

import helpers.{UnitSpec, WithApplication}
import pages.disposal_of_vehicle._
import play.api.test.FakeRequest
import play.api.test.Helpers._

final class BeforeYouStartUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val result = beforeYouStart.present(FakeRequest())
      status(result) should equal(OK)
    }

    "display expected progress bar" in new WithApplication {
      val result = beforeYouStart.present(FakeRequest())
      contentAsString(result) should include("Step 1 of 6")
    }

    "display prototype message when config set to true" in new WithApplication {
      val result = beforeYouStart.present(FakeRequest())
      contentAsString(result) should include("""<div class="prototype">""")
    }
  }

  "submit" should {
    "redirect to next page after the button is clicked" in new WithApplication {
      val result = beforeYouStart.submit(FakeRequest())
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  private val beforeYouStart = injector.getInstance(classOf[BeforeYouStart])
}
