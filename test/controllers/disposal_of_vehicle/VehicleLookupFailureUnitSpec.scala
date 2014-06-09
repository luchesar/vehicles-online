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
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel())
      val result = vehicleLookupFailure.present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to setuptraderdetails when cache is empty" in new WithApplication {
      val request = FakeRequest()
      val result = vehicleLookupFailure.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to setuptraderdetails on if only BusinessChooseYourAddress cache is populated" in new WithApplication {
      val request = FakeRequest()
      val result = vehicleLookupFailure.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to setuptraderdetails on if only VehicleLookupFormModelCache is populated" in new WithApplication {
      val request = FakeRequest().
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

  "withLanguageEn" should {
    "redirect back to the same page" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.bruteForcePreventionViewModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel())
      val result = vehicleLookupFailure.withLanguageEn(request)
      whenReady(result) {
        r =>
          r.header.status should equal(SEE_OTHER) // Redirect...
          r.header.headers.get(LOCATION) should equal(Some(VehicleLookupFailurePage.address)) // ... back to the same page.
      }
    }

    "writes language cookie set to 'en'" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.bruteForcePreventionViewModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel())
      val result = vehicleLookupFailure.withLanguageEn(request)
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.find(_.name == Play.langCookieName) match {
            case Some(cookie) => cookie.value should equal("en")
            case None => fail("langCookieName not found")
          }
      }
    }
  }
  
  private val vehicleLookupFailure = {
    injector.getInstance(classOf[VehicleLookupFailure])
  }
}
