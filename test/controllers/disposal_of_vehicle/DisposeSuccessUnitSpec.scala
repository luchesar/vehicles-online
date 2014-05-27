package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import helpers.UnitSpec
import scala.Some
import composition.{testInjector => injector}

final class DisposeSuccessUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeTransactionId()).
        withCookies(CookieFactoryForUnitSpecs.vehicleRegistrationNumber()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel())

      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to SetUpTradeDetails on present when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only DisposeDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel())
      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails and DisposeDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeFormModel())
      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails and DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only DisposeDetails and DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel())
      val result = disposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  "submit" should {
    "redirect to correct next page after the new disposal button is clicked" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody("action" -> mappings.disposal_of_vehicle.DisposeSuccess.NewDisposalAction).
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeTransactionId()).
        withCookies(CookieFactoryForUnitSpecs.vehicleRegistrationNumber()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel())
      val result = disposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to correct next page after the exit button is clicked" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody("action" -> mappings.disposal_of_vehicle.DisposeSuccess.ExitAction).
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeTransactionId()).
        withCookies(CookieFactoryForUnitSpecs.vehicleRegistrationNumber()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel())
      val result = disposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(BeforeYouStartPage.address))
      }
    }

    "display action not allowed when trying to submit with incorrect action" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody("action" -> "RUBBISH_ACTION")
      val result = disposeSuccess.submit(request)
      val content = contentAsString(result)
      content should equal(ActionNotAllowedMessage)
    }

    "handle a form post in which no action is specified" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody()
      val result = disposeSuccess.submit(request)
      val content = contentAsString(result)
      content should equal(ActionNotAllowedMessage)
    }


    "redirect to SetUpTradeDetails on submit when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody("action" -> mappings.disposal_of_vehicle.DisposeSuccess.NewDisposalAction)
      val result = disposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody("action" -> mappings.disposal_of_vehicle.DisposeSuccess.NewDisposalAction).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val result = disposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody("action" -> mappings.disposal_of_vehicle.DisposeSuccess.NewDisposalAction).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only DisposeDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody("action" -> mappings.disposal_of_vehicle.DisposeSuccess.NewDisposalAction).
        withCookies(CookieFactoryForUnitSpecs.disposeModel())
      val result = disposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails and DisposeDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody("action" -> mappings.disposal_of_vehicle.DisposeSuccess.NewDisposalAction).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeFormModel())
      val result = disposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails and DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody("action" -> mappings.disposal_of_vehicle.DisposeSuccess.NewDisposalAction).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only DisposeDetails and DealerDetails are cached" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody("action" -> mappings.disposal_of_vehicle.DisposeSuccess.NewDisposalAction).
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel())
      val result = disposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }

  private final val ActionNotAllowedMessage = "This action is not allowed"

  private val disposeSuccess = injector.getInstance(classOf[DisposeSuccess])
}
