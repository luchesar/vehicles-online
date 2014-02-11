package views.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class AreYouRegisteredIntegrationSpec extends Specification with Tags {

  "AreYouRegistered Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/are-you-registered")

      // Assert
      titleMustEqual("Change of keeper address4")

    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      browser.goTo("/are-you-registered")

      // Act
      browser.submit("button[type='submit']")

      // Assert
      titleMustEqual("Change of keeper address5")
    }

  }

}