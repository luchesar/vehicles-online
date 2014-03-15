package views.disposal_of_vehicle

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle._
import helpers.UiSpec

class DisposeFailureIntegrationSpec extends UiSpec {
  "DisposeFailureIntegration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      DisposeFailurePage.cacheSetup
      browser.goTo(DisposeFailurePage.url)

      titleMustEqual(DisposeFailurePage.title)
    }

    "redirect to setuptrade details if cache is empty on page load" in new WithBrowser with BrowserMatchers {
      browser.goTo(DisposeFailurePage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WithBrowser with BrowserMatchers {
      DisposeFailurePage.cacheSetup
      browser.goTo(DisposeFailurePage.url)

      browser.click("#vehiclelookup")

      titleMustEqual(VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when button clicked" in new WithBrowser with BrowserMatchers {
      DisposeFailurePage.cacheSetup
      browser.goTo(DisposeFailurePage.url)

      browser.click("#setuptradedetails")

      titleMustEqual(SetUpTradeDetailsPage.title)
    }
  }
}
