package views.disposal_of_vehicle

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle._

class DisposeFailureIntegrationSpec extends Specification with Tags {
  "DisposeFailureIntegration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      DisposeFailurePage.cacheSetup
      browser.goTo(DisposeFailurePage.url)

      // Assert
      titleMustEqual(DisposeFailurePage.title)
    }

    "redirect to setuptrade details if cache is empty on page load" in new WithBrowser with BrowserMatchers {
      //Arrange & Act
      browser.goTo(DisposeFailurePage.url)

      //Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      DisposeFailurePage.cacheSetup
      browser.goTo(DisposeFailurePage.url)

      // Act
      browser.click("#vehiclelookup")

      // Assert
      titleMustEqual(VehicleLookupPage.title)
    }

    "redirect to setuptradedetails when button clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      DisposeFailurePage.cacheSetup
      browser.goTo(DisposeFailurePage.url)

      // Act
      browser.click("#setuptradedetails")

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }
  }
}
