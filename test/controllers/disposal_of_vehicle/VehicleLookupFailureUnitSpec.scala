package controllers.disposal_of_vehicle

import helpers.UnitSpec
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import pages.disposal_of_vehicle._
import play.api.test.Helpers._
import play.api.test.FakeRequest
import composition.TestComposition.{testInjector => injector}
import helpers.WithApplication
import services.fakes.FakeVehicleLookupWebService._
import scala.Some
import common.CookieHelper._
import scala.Some
import play.api.Play

final class VehicleLookupFailureUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.bruteForcePreventionViewModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupResponseCode())
      val result = vehicleLookupFailure.present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to setuptraderdetails on if traderDetailsModel is not in cache" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.bruteForcePreventionViewModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupResponseCode())
      val result = vehicleLookupFailure.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to setuptraderdetails on if bruteForcePreventionViewModel is not in cache" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupResponseCode())
      val result = vehicleLookupFailure.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to setuptraderdetails on if VehicleLookupFormModelCache is not in cache" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.bruteForcePreventionViewModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupResponseCode())
      val result = vehicleLookupFailure.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to setuptraderdetails on if only vehicleLookupResponseCode is not in cache" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.bruteForcePreventionViewModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel())
      val result = vehicleLookupFailure.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  "submit" should {
    "redirect to vehiclelookup on submit" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel())
      val result = vehicleLookupFailure.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to setuptraderdetails on submit when cache is empty" in new WithApplication {
      val request = FakeRequest()
      val result = vehicleLookupFailure.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(BeforeYouStartPage.address))
      }
    }
  }
  
  private val vehicleLookupFailure = {
    injector.getInstance(classOf[VehicleLookupFailure])
  }
}
