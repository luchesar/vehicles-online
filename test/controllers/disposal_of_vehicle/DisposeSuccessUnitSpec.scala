package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.{CookieFactory, CacheSetup}
import helpers.UnitSpec
import services.session.{SessionState, PlaySessionState}

class DisposeSuccessUnitSpec extends UnitSpec {

  "Disposal success controller" should {

    "present" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.setupTradeDetails()).
        withCookies(CookieFactory.dealerDetails()).
        withCookies(CookieFactory.vehicleDetailsModel()).
        withCookies(CookieFactory.disposeFormModel()).
        withCookies(CookieFactory.disposeTransactionId()).
        withCookies(CookieFactory.vehicleRegistrationNumber()).
        withCookies(CookieFactory.disposeModel())

      val result = disposeSuccess().present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to next page after the new disposal button is clicked" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.setupTradeDetails()).
        withCookies(CookieFactory.dealerDetails()).
        withCookies(CookieFactory.vehicleDetailsModel()).
        withCookies(CookieFactory.disposeFormModel()).
        withCookies(CookieFactory.disposeTransactionId()).
        withCookies(CookieFactory.vehicleRegistrationNumber()).
        withCookies(CookieFactory.disposeModel())
      val result = disposeSuccess().newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = disposeSuccess().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.setupTradeDetails()).
        withCookies(CookieFactory.dealerDetails())
      val result = disposeSuccess().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.setupTradeDetails()).
        withCookies(CookieFactory.vehicleDetailsModel())
      val result = disposeSuccess().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only DisposeDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.setupTradeDetails()).
        withCookies(CookieFactory.disposeModel())
      val result = disposeSuccess().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails and DisposeDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.vehicleDetailsModel()).
        withCookies(CookieFactory.disposeFormModel())
      val result = disposeSuccess().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails and DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.vehicleDetailsModel())
      val result = disposeSuccess().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only DisposeDetails and DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.dealerDetails()).
        withCookies(CookieFactory.disposeModel())
      val result = disposeSuccess().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = disposeSuccess().newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.dealerDetails())
      val result = disposeSuccess().newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.vehicleDetailsModel())
      val result = disposeSuccess().newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only DisposeDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.disposeModel())
      val result = disposeSuccess().newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails and DisposeDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.vehicleDetailsModel()).
        withCookies(CookieFactory.disposeFormModel())
      val result = disposeSuccess().newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails and DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.vehicleDetailsModel())
      val result = disposeSuccess().newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only DisposeDetails and DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactory.setupTradeDetails()).
        withCookies(CookieFactory.dealerDetails()).
        withCookies(CookieFactory.disposeModel())
      val result = disposeSuccess().newDisposal(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  private def disposeSuccess() =
    new DisposeSuccess()
}
