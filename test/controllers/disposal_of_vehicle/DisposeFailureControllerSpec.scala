package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import play.api.test.{FakeRequest, WithApplication}
import controllers.disposal_of_vehicle
import play.api.test.Helpers._
import helpers.disposal_of_vehicle.{SetUpTradeDetailsPage, VehicleLookupPage, DisposeFailurePage}

class DisposeFailureControllerSpec extends WordSpec with Matchers with Mockito {

  "DisposalFailure - Controller" should {

    "present" in new WithApplication {
      DisposeFailurePage.cacheSetup()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeFailure.present(request)

      status(result) should equal(OK)
    }

    "redirect to vehicle lookup page when button clicked" in new WithApplication {
      DisposeFailurePage.cacheSetup()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeFailure.submit(request)

      redirectLocation(result) should equal(Some(VehicleLookupPage.url))
    }

    "redirect to setuptraderdetails when no details are in cache and submit is selected" in new WithApplication() {
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeFailure.submit(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }
  }
}
