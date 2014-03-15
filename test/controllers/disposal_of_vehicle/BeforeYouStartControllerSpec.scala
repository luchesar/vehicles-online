package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import helpers.disposal_of_vehicle.SetUpTradeDetailsPage
import helpers.UnitSpec

class BeforeYouStartUnitSpec extends UnitSpec {

  "BeforeYouStart - Controller" should {

    "present" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.BeforeYouStart.present(request)

      status(result) should equal(OK)
    }

    "redirect to next page after the button is clicked" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.BeforeYouStart.submit(request)

      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }
  }
}