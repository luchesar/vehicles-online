package controllers.disposal_of_vehicle

import common.ClientSideSessionFactory
import helpers.common.CookieHelper
import mappings.common.PreventGoingToDisposePage.PreventGoingToDisposePageCacheKey
import org.mockito.Mockito.when
import play.api.test.Helpers._
import pages.disposal_of_vehicle.{VehicleLookupPage, SetupTradeDetailsPage, BeforeYouStartPage}
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import helpers.UnitSpec
import helpers.WithApplication
import play.api.test.FakeRequest
import CookieHelper.fetchCookiesFromHeaders
import utils.helpers.Config

final class DisposeSuccessUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      whenReady(present) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to SetUpTradeDetails on present when cache is empty" in new WithApplication {
      val request = FakeRequest()
      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails are cached" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only DisposeDetails are cached" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel())
      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails and DisposeDetails are cached" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeFormModel())
      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails and DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only DisposeDetails and DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel())
      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "display expected progress bar" in new WithApplication {
      contentAsString(present) should include("Step 6 of 6")
    }

    "display prototype message when config set to true" in new WithApplication {
      contentAsString(present) should include("""<div class="prototype">""")
    }

    "not display prototype message when config set to false" in new WithApplication {
      val request = FakeRequest()
      implicit val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
      implicit val config: Config = mock[Config]
      when(config.isPrototypeBannerVisible).thenReturn(false) // Stub this config value.
      val disposeSuccessPrototypeNotVisible = new DisposeSuccess()

      val result = disposeSuccessPrototypeNotVisible.present(request)
      contentAsString(result) should not include """<div class="prototype">"""
    }
  }

  "newDisposal" should {
    "redirect to correct next page after the new disposal button is clicked" in new WithApplication {
      val result = disposeSuccess.newDisposal(requestFullyPopulated)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when cache is empty" in new WithApplication {
      val request = FakeRequest()
      val result = disposeSuccess.newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val result = disposeSuccess.newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails are cached" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeSuccess.newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only DisposeDetails are cached" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.disposeModel())
      val result = disposeSuccess.newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails and DisposeDetails are cached" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeFormModel())
      val result = disposeSuccess.newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails and DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeSuccess.newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only DisposeDetails and DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel())
      val result = disposeSuccess.newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "write interstitial cookie with BeforeYouStart url" in new WithApplication {
      val result = disposeSuccess.newDisposal(requestFullyPopulated)
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.exists(c => c.name == PreventGoingToDisposePageCacheKey) should equal(true)
      }
    }
  }

  "exit" should {
    "redirect to BeforeYouStartPage" in new WithApplication {
      val result = disposeSuccess.exit(requestFullyPopulated)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(BeforeYouStartPage.address))
      }
    }

    "write interstitial cookie with BeforeYouStart url" in new WithApplication {
      val result = disposeSuccess.exit(requestFullyPopulated)
      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.exists(c => c.name == PreventGoingToDisposePageCacheKey) should equal(true)
      }
    }
  }

  private val disposeSuccess = injector.getInstance(classOf[DisposeSuccess])
  private val requestFullyPopulated = FakeRequest().
    withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
    withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
    withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
    withCookies(CookieFactoryForUnitSpecs.disposeFormModel()).
    withCookies(CookieFactoryForUnitSpecs.disposeTransactionId()).
    withCookies(CookieFactoryForUnitSpecs.vehicleRegistrationNumber()).
    withCookies(CookieFactoryForUnitSpecs.disposeModel())
  private lazy val present = disposeSuccess.present(requestFullyPopulated)
}
