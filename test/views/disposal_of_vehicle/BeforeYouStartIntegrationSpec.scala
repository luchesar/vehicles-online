package views.disposal_of_vehicle

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.BeforeYouStart

class BeforeYouStartIntegrationSpec extends Specification with Tags {

  "BeforeYouStart Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo(BeforeYouStart.url)

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade: start")
    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      browser.goTo(BeforeYouStart.url)

      // Act
      browser.click("#next")

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade: set-up")
    }
  }
}