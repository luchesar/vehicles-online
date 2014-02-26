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
      browser.goTo(DisposeFailure.url)

      // Assert
      titleMustEqual(DisposeFailure.title)
    }

    "go to vehicle lookup when button clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      browser.goTo(DisposeFailure.url)

      // Act
      browser.click("#next")

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
      //ToDo this needs to look at vehicle lookup page once cache is passed to controller
    }
  }
}
