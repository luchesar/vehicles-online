package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import helpers.UnitSpec

class DisposeFailureUnitSpec extends UnitSpec {

  "DisposalFailure - Controller" should {
    "present" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.dealerDetails()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeTransactionId())
      val result = disposeFailure().present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to vehicle lookup page when button clicked" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.dealerDetails()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeTransactionId())
      val result = disposeFailure().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to setuptraderdetails when no details are in cache and submit is selected" in new WithApplication() {
      val request = FakeRequest().withSession()
      val result = disposeFailure().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  private def disposeFailure() =
    new DisposeFailure()
}

