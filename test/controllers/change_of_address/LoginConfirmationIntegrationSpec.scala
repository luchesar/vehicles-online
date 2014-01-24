package controllers.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class LoginConfirmationIntegrationSpec extends Specification with Tags {

  "LoginConfirmation Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/login-confirmation")

      // Assert
      titleMustContain("Login confirmation")
    }

//    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
//      // Arrange
//      browser.goTo("/login-confirmation")
//
//      // Act
//      browser.submit("button[type='submit']")
//
//      // Assert 
//      titleMustEqual("Login confirmation") // TODO check the title of the next page - currently redirecting to original
//    }

  }

}