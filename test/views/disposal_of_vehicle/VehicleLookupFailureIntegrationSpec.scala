package views.disposal_of_vehicle

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle._

class VehicleLookupFailureIntegrationSpec extends Specification with Tags {
  "DisposeFailureIntegration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      //Arrange & Act
      VehicleLookupFailurePage.cacheSetupHappyPath(browser)
      browser.goTo(VehicleLookupFailurePage.url)

      // Assert
      titleMustEqual(VehicleLookupFailurePage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      VehicleLookupFailurePage.cacheSetupHappyPath(browser)
      browser.goTo(VehicleLookupFailurePage.url)

      // Act
      browser.click("#next")

      // Assert
      titleMustEqual(VehicleLookupPage.title)
    }

    "redirect to setuptraderdetails page when no details are cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo(VehicleLookupFailurePage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }
  }
}
