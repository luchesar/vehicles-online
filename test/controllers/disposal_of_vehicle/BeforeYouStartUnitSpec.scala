package controllers.disposal_of_vehicle

import common.CookieHelper._
import composition.TestComposition.{testInjector => injector}
import helpers.UnitSpec
import helpers.WithApplication
import pages.disposal_of_vehicle._
import play.api.Play
import play.api.test.FakeRequest
import play.api.test.Helpers._

final class BeforeYouStartUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val result = beforeYouStart.present(FakeRequest())
      status(result) should equal(OK)
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
