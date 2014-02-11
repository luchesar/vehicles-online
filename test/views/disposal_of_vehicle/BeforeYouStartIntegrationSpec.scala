package views.disposal_of_vehicle

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class BeforeYouStartIntegrationSpec extends Specification with Tags {

  "BeforeYouStart Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/disposal-of-vehicle/before-you-start")

      // Assert
      titleMustContain("Dispose a vehicle into the motor trade 1")
    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      browser.goTo("/disposal-of-vehicle/before-you-start")

      // Act
      browser.click("#next")

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade 2")
    }
  }
}