package controllers.disposal_of_vehicle

import helpers.UnitSpec
import pages.disposal_of_vehicle._
import play.api.test.Helpers._
import play.api.test.{FakeRequest, WithApplication}
import utils.helpers.{CookieNameHashing, NoHash, CookieEncryption, NoEncryption}

final class BeforeYouStartUnitSpec extends UnitSpec {

  "BeforeYouStart - Controller" should {

    "present" in new WithApplication {
      val result = beforeYouStart.present(newFakeRequest)
      status(result) should equal(OK)
    }

    "redirect to next page after the button is clicked" in new WithApplication {
      val result = beforeYouStart.submit(newFakeRequest)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  def newFakeRequest = {
    FakeRequest().withSession()
  }

  private def beforeYouStart = {
    val noCookieEncryption = new NoEncryption with CookieEncryption
    val noCookieNameHashing = new NoHash with CookieNameHashing
    new BeforeYouStart()(noCookieEncryption, noCookieNameHashing)
  }
}
