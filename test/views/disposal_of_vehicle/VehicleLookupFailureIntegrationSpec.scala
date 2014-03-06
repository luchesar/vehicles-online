package views.disposal_of_vehicle

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle._

class VehicleLookupFailureIntegrationSpec extends Specification with Tags {
  "DisposeFailureIntegration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      //Arrange & Act
      VehicleLookupFailurePage.cacheSetup()
      browser.goTo(VehicleLookupFailurePage.url)

      // Assert
      titleMustEqual(VehicleLookupFailurePage.title)
    }

    "redirect to setuptrade details if cache is empty on page load" in new WithBrowser with BrowserMatchers {
      //Arrange & Act
      browser.goTo(VehicleLookupFailurePage.url)

      //Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only VehicleLookupFormModelCache is populated" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      VehicleLookupPage.setupVehicleLookupFormModelCache()
      browser.goTo(VehicleLookupFailurePage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect to setuptrade details if only BusinessChooseYourAddress cache is populated" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache()
      browser.goTo(VehicleLookupFailurePage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      VehicleLookupFailurePage.cacheSetup()
      browser.goTo(VehicleLookupFailurePage.url)

      // Act
      browser.click("#vehiclelookup")

      // Assert
      titleMustEqual(VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when button clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      VehicleLookupFailurePage.cacheSetup()
      browser.goTo(VehicleLookupFailurePage.url)

      // Act
      browser.click("#setuptradedetails")

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }
  }
}
