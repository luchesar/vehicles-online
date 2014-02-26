package views.disposal_of_vehicle

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle._

class DisposeFailureIntegrationSpec extends Specification with Tags {
  "DisposeFailureIntegration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      DisposePage.setupCache
      browser.goTo(DisposeFailurePage.url)

      // Assert
      titleMustEqual(DisposeFailurePage.title)
    }

    "redirect to vehiclelookup when button clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      SetUpTradeDetailsPage.setupCache
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      DisposePage.setupCache
      browser.goTo(DisposeFailurePage.url)

      // Act
      browser.click("#next")

      // Assert
      titleMustEqual(VehicleLookupPage.title)
    }

    "redirect to setuptraderdetails page when no details are cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo(DisposeFailurePage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }
  }
}
