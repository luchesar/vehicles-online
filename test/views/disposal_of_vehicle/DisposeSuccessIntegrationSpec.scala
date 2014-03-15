package views.disposal_of_vehicle

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle._
import helpers.UiSpec

class DisposeSuccessIntegrationSpec extends UiSpec {
  "Dispose confirmation integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      DisposeFailurePage.cacheSetup
      browser.goTo(DisposeSuccessPage.url)

      titleMustEqual(DisposeSuccessPage.title)
    }

    "redirect when no details are cached" in new WithBrowser with BrowserMatchers {
      browser.goTo(DisposeSuccessPage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect when only DealerDetails are cached" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      browser.goTo(DisposeSuccessPage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect when only VehicleDetails are cached" in new WithBrowser with BrowserMatchers {
      VehicleLookupPage.setupVehicleDetailsModelCache()
      browser.goTo(DisposeSuccessPage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails are cached" in new WithBrowser with BrowserMatchers {
      DisposePage.setupDisposeFormModelCache()
      browser.goTo(DisposeSuccessPage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect when only DealerDetails and VehicleDetails are cached" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.setupVehicleDetailsModelCache()
      browser.goTo(DisposeSuccessPage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails and VehicleDetails are cached" in new WithBrowser with BrowserMatchers {
      DisposePage.setupDisposeFormModelCache()
      VehicleLookupPage.setupVehicleDetailsModelCache()
      browser.goTo(DisposeSuccessPage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect when only DisposeDetails and DealerDetails are cached" in new WithBrowser with BrowserMatchers {
      DisposePage.setupDisposeFormModelCache()
      BusinessChooseYourAddressPage.setupCache()
      browser.goTo(DisposeSuccessPage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "display vehicle lookup page when new disposal link is clicked" in new WithBrowser with BrowserMatchers {
      DisposeFailurePage.cacheSetup
      browser.goTo(DisposeSuccessPage.url)

      browser.click("#newDisposal")

      titleMustEqual(VehicleLookupPage.title)
    }
  }
}
