package views.disposal_of_vehicle

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.{UprnNotFoundPage, SetUpTradeDetailsPage, EnterAddressManuallyPage}
import helpers.UiSpec

class UprnNotFoundIntegrationSpec extends UiSpec {

  "UprnNotFound Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo(UprnNotFoundPage.url)

      titleMustEqual(UprnNotFoundPage.title)
    }

    "go to setuptradedetails page after the Setup Trade Details button is clicked" in new WithBrowser with BrowserMatchers {
      browser.goTo(UprnNotFoundPage.url)

      browser.click("#setuptradedetailsbutton")

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "go to manualaddress page after the Manual Address button is clicked and trade details have been set up in cache" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      browser.goTo(UprnNotFoundPage.url)

      browser.click("#manualaddressbutton")

      titleMustEqual(EnterAddressManuallyPage.title)
    }

    "go to setuptradedetails page after the Manual Address button is clicked and trade details have not been set up in cache" in new WithBrowser with BrowserMatchers {
      browser.goTo(UprnNotFoundPage.url)

      browser.click("#manualaddressbutton")

      titleMustEqual(SetUpTradeDetailsPage.title)
    }
  }
}