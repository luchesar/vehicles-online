package views.disposal_of_vehicle

import org.specs2.mutable.Specification
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import pages.common.ErrorPanel
import helpers.UiSpec

class DisposeIntegrationSpec extends UiSpec with TestHarness {

  "Dispose Integration" should {

    "be presented" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      go to DisposePage.url

      assert(page.title equals DisposePage.title)
    }

    "display DisposeSuccess page on correct submission" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      CacheSetup.vehicleLookupFormModel()
      DisposePage.happyPath

      assert(page.title equals DisposeSuccessPage.title)
    }

    "display validation errors when no fields are completed" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      DisposePage.sadPath

      assert(ErrorPanel.numberOfErrors equals 3)
    }

    "redirect when no VehicleLookupFormModel is cached" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      go to DisposePage.url

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "redirect when no traderBusinessName is cached" in new WebBrowser {
      go to DisposePage.url

      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "display validation errors when month and year are input but no day" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      go to DisposePage.url

      DisposePage.dateOfDisposalMonth select "12"
      DisposePage.dateOfDisposalYear enter "2013"
      click on DisposePage.dispose

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation errors when day and year are input but no month" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      go to DisposePage.url

      DisposePage.dateOfDisposalDay select "12"
      DisposePage.dateOfDisposalYear enter "2013"
      click on DisposePage.dispose

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display validation errors when day and month are input but no year" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      go to DisposePage.url

      DisposePage.dateOfDisposalDay select "12"
      DisposePage.dateOfDisposalMonth select "2"
      click on DisposePage.dispose

      assert(ErrorPanel.numberOfErrors equals 1)
    }

    "display previous page when back link is clicked" in new WebBrowser {
      CacheSetup.businessChooseYourAddress()
      CacheSetup.vehicleDetailsModel()
      go to DisposePage.url

      click on DisposePage.back

      assert(page.title equals VehicleLookupPage.title)
    }
  }
}
