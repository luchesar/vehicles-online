package views.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class SignInProviderIntegrationSpec extends Specification with Tags {

  "SignInProvider Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/sign-in-provider")

      // Assert
      titleMustEqual("Change of keeper address5")

    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      browser.goTo("/sign-in-provider")

      // Act
      browser.submit("button[type='submit']")

      // Assert
      titleMustEqual("Verified login id")
    }

  }

}