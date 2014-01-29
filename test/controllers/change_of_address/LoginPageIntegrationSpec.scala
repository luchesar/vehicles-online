package controllers.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class LoginPageIntegrationSpec extends Specification with Tags {

  "LoginPage Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/login-page")

      // Assert
      titleMustContain("Verified login id")
    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      browser.goTo("/login-page")

      // Act
      browser.fill("#username") `with` "roger"
      browser.fill("#password") `with` "examplepassword"
      browser.submit("button[type='submit']")

      // Assert the title from the next page
      titleMustEqual("Login confirmation")
    }

  }

}