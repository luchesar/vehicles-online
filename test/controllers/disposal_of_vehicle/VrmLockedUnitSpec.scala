package controllers.disposal_of_vehicle

import helpers.common.CookieHelper
import helpers.{WithApplication, UnitSpec}
import composition.TestComposition.{testInjector => injector}
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import services.fakes.FakeDateServiceImpl
import play.api.test.Helpers._
import scala.Some
import pages.disposal_of_vehicle.{VrmLockedPage, BeforeYouStartPage, VehicleLookupPage, SetupTradeDetailsPage}
import play.api.test.FakeRequest
import CookieHelper._
import play.api.Play

final class VrmLockedUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val dateService = new FakeDateServiceImpl
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.bruteForcePreventionViewModel(dateTimeISOChronology = dateService.dateTimeISOChronology))
      val result = vrmLocked.present(request)

      whenReady(result) {
        r => r.header.status should equal(play.api.http.Status.OK)
      }
    }
  }

  "newDisposal" should {
    "redirect to vehicle lookup page after the new disposal button is clicked when the expected data is in the cookies" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val result = vrmLocked.newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to setup trade details page after the new disposal button is clicked when the expected data is not in the cookies" in new WithApplication {
      val request = FakeRequest()
      val result = vrmLocked.newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  "exit" should {
    "redirect to correct next page after the exit button is clicked" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val result = vrmLocked.exit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(BeforeYouStartPage.address))
      }
    }
  }

  private val vrmLocked = injector.getInstance(classOf[VrmLocked])
}
