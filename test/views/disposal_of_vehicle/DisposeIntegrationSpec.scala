package views.disposal_of_vehicle

import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import pages.common.ErrorPanel
import helpers.UiSpec
import services.fakes.FakeDateServiceImpl._
import DisposePage._
import services.session.{SessionState, PlaySessionState}
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState
import org.openqa.selenium.WebDriver

class DisposeIntegrationSpec extends UiSpec with TestHarness {

  "Dispose Integration" should {

    "be presented" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      go to DisposePage

      assert(page.title equals title)
    }

    "display DisposeSuccess page on correct submission" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner).
        vehicleLookupFormModelIntegration()

      happyPath

      assert(page.title equals DisposeSuccessPage.title)
    }

    "display validation errors when no data is entered" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)

      sadPath

      assert(ErrorPanel.numberOfErrors equals 3)
    }

    "redirect when no vehicleDetailsModel is cached" in new WebBrowser {
      go to BeforeYouStartPage
      new CacheSetup(newSessionState.inner).
        dealerDetailsIntegration()

      go to DisposePage

      assert(page.title equals VehicleLookupPage.title)
    }

    "redirect when no businessChooseYourAddress is cached" in new WebBrowser {
      go to BeforeYouStartPage
      new CacheSetup(newSessionState.inner).
        vehicleDetailsModel()

      go to DisposePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when no traderBusinessName is cached" in new WebBrowser {
      go to DisposePage

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "display validation errors when month and year are input but no day" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)
      go to DisposePage
      dateOfDisposalMonth select dateOfDisposalMonthValid
      dateOfDisposalYear select dateOfDisposalYearValid

      click on consent
      click on lossOfRegistrationConsent
      click on dispose

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation errors when day and year are input but no month" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)
      go to DisposePage
      dateOfDisposalDay select dateOfDisposalDayValid
      dateOfDisposalYear select dateOfDisposalYearValid

      click on consent
      click on lossOfRegistrationConsent
      click on dispose

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation errors when day and month are input but no year" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)
      go to DisposePage
      dateOfDisposalDay select dateOfDisposalDayValid
      dateOfDisposalMonth select dateOfDisposalMonthValid

      click on consent
      click on lossOfRegistrationConsent
      click on dispose

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display previous page when back link is clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup(newSessionState.inner)
      go to DisposePage

      click on back

      assert(page.title equals VehicleLookupPage.title)
    }
  }

  private def cacheSetup(sessionState: SessionState)(implicit webDriver: WebDriver) = {
    new CacheSetup(sessionState).
      dealerDetailsIntegration().
      vehicleDetailsModel()
  }

  private def newSessionState = {
    val sessionState = new PlaySessionState()
    new DisposalOfVehicleSessionState(sessionState)
  }
}
