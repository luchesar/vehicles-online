package views.disposal_of_vehicle

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.SetUpTradeDetailsPopulate

class DisposeConfirmationIntegrationSpec extends Specification with Tags {

  val disposeConfirmationUrl = "/disposal-of-vehicle/dispose-confirmation"

  "Dispose confirmation integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPopulate.happyPath(browser)
      browser.goTo(disposeConfirmationUrl)

      // Check the page title is correct
      titleMustEqual("Dispose a vehicle into the motor trade: summary")
    }
  }

  "Redirect when no traderBusinessName is cached" in new WithBrowser with BrowserMatchers {
    // Arrange & Act
    browser.goTo(disposeConfirmationUrl)

    // Assert
    titleMustEqual("Dispose a vehicle into the motor trade: set-up")
  }
}
