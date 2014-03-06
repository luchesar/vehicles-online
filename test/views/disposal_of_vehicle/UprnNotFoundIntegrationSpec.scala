package views.disposal_of_vehicle

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.{UprnNotFoundPage, SetUpTradeDetailsPage, EnterAddressManuallyPage}

class UprnNotFoundIntegrationSpec extends Specification with Tags {

  "UprnNotFound Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo(UprnNotFoundPage.url)

      // Assert
      titleMustEqual(UprnNotFoundPage.title)
    }

    "go to setuptradedetails page after the Setup Trade Details button is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      browser.goTo(UprnNotFoundPage.url)

      // Act
      browser.click("#setuptradedetailsbutton")

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "go to manualaddress page after the Manual Address button is clicked and trade details have been set up in cache" in new WithBrowser with BrowserMatchers {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      browser.goTo(UprnNotFoundPage.url)

      // Act
      browser.click("#manualaddressbutton")

      // Assert
      titleMustEqual(EnterAddressManuallyPage.title)
    }

    "go to setuptradedetails page after the Manual Address button is clicked and trade details have not been set up in cache" in new WithBrowser with BrowserMatchers {
      // Arrange
      browser.goTo(UprnNotFoundPage.url)

      // Act
      browser.click("#manualaddressbutton")

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }
  }
}