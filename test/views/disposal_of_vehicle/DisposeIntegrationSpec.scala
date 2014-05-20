package views.disposal_of_vehicle

import pages.disposal_of_vehicle.DisposePage._
import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.TestHarness
import org.openqa.selenium.WebDriver
import pages.common.ErrorPanel
import pages.disposal_of_vehicle._
import services.fakes.FakeDateServiceImpl._

class DisposeIntegrationSpec extends UiSpec with TestHarness {

  "Dispose Integration" should {

    "be presented" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to DisposePage

      page.title should equal(title)
    }

    "display DisposeSuccess page on correct submission" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup().
        vehicleLookupFormModelIntegration()

      happyPath

      page.title should equal(DisposeSuccessPage.title)
    }

    "display validation errors when no data is entered" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      sadPath

      ErrorPanel.numberOfErrors should equal(3)
    }

    "redirect when no vehicleDetailsModel is cached" in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.dealerDetailsIntegration()

      go to DisposePage

      page.title should equal(VehicleLookupPage.title)
    }

    "redirect when no businessChooseYourAddress is cached" in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.vehicleDetailsModelIntegration()

      go to DisposePage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "redirect when no traderBusinessName is cached" in new WebBrowser {
      go to DisposePage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "display validation errors when month and year are input but no day" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposePage
      dateOfDisposalMonth select dateOfDisposalMonthValid
      dateOfDisposalYear select dateOfDisposalYearValid

      click on consent
      click on lossOfRegistrationConsent
      click on dispose

      ErrorPanel.numberOfErrors should equal(1)
    }

    "display validation errors when day and year are input but no month" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposePage
      dateOfDisposalDay select dateOfDisposalDayValid
      dateOfDisposalYear select dateOfDisposalYearValid

      click on consent
      click on lossOfRegistrationConsent
      click on dispose

      ErrorPanel.numberOfErrors should equal(1)
    }

    "display validation errors when day and month are input but no year" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposePage
      dateOfDisposalDay select dateOfDisposalDayValid
      dateOfDisposalMonth select dateOfDisposalMonthValid

      click on consent
      click on lossOfRegistrationConsent
      click on dispose

      ErrorPanel.numberOfErrors should equal(1)
    }

    "display previous page when back link is clicked" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to DisposePage

      click on back

      page.title should equal(VehicleLookupPage.title)
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) = {
    CookieFactoryForUISpecs.
      dealerDetailsIntegration().
      vehicleDetailsModelIntegration()
  }
}
