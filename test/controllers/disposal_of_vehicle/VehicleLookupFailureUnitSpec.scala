package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import scala.Some
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.{CookieFactory, CacheSetup}
import helpers.UnitSpec
import services.session.PlaySessionState

class VehicleLookupFailureUnitSpec extends UnitSpec {

  "VehicleLookupFailurePage - Controller" should {

    "present" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.dealerDetails()).
        withCookies(CookieFactory.vehicleLookupFormModel())
      val result = vehicleLookupFailure().present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to vehiclelookup on submit" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.dealerDetails()).
        withCookies(CookieFactory.vehicleLookupFormModel())
      val result = vehicleLookupFailure().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to setuptraderdetails when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = vehicleLookupFailure().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to setuptraderdetails on submit when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = vehicleLookupFailure().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(BeforeYouStartPage.address))
      }
    }

    "redirect to setuptraderdetails on if only BusinessChooseYourAddress cache is populated" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = vehicleLookupFailure().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to setuptraderdetails on if only VehicleLookupFormModelCache is populated" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.vehicleLookupFormModel())
      val result = vehicleLookupFailure().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }
  
  private def vehicleLookupFailure() =
    new VehicleLookupFailure()


}
