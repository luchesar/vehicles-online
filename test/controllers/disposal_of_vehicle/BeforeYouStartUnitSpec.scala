package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import pages.disposal_of_vehicle._
import helpers.UnitSpec
import play.mvc.SimpleResult
import scala.concurrent.Future

class BeforeYouStartUnitSpec extends UnitSpec {

  "BeforeYouStart - Controller" should {

    "present" in new WithApplication {
      val result = disposal_of_vehicle.BeforeYouStart.present(newFakeRequest)
      status(result) should equal(OK)
    }

    "redirect to next page after the button is clicked" in new WithApplication {
      val result = disposal_of_vehicle.BeforeYouStart.submit(newFakeRequest)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  def newFakeRequest = {
    FakeRequest().withSession()
  }
}