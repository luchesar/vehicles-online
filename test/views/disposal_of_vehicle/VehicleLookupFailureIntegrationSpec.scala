package views.disposal_of_vehicle

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle._
import helpers.UiSpec

class VehicleLookupFailureIntegrationSpec extends UiSpec {
  "DisposeFailureIntegration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      VehicleLookupFailurePage.cacheSetup()
      browser.goTo(VehicleLookupFailurePage.url)

      titleMustEqual(VehicleLookupFailurePage.title)
    }

    "redirect to setuptrade details if cache is empty on page load" in new WithBrowser with BrowserMatchers {
      browser.goTo(VehicleLookupFailurePage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only VehicleLookupFormModelCache is populated" in new WithBrowser with BrowserMatchers {
      VehicleLookupPage.setupVehicleLookupFormModelCache()
      browser.goTo(VehicleLookupFailurePage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only BusinessChooseYourAddress cache is populated" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      browser.goTo(VehicleLookupFailurePage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WithBrowser with BrowserMatchers {
      VehicleLookupFailurePage.cacheSetup()
      browser.goTo(VehicleLookupFailurePage.url)

      browser.click("#vehiclelookup")

      titleMustEqual(VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when button clicked" in new WithBrowser with BrowserMatchers {
      VehicleLookupFailurePage.cacheSetup()
      browser.goTo(VehicleLookupFailurePage.url)

      browser.click("#setuptradedetails")

      titleMustEqual(SetUpTradeDetailsPage.title)
    }
  }
}
