package controllers.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.{Formulate, BrowserMatchers}

class LoginPageIntegrationSpec extends Specification with Tags {

  "LoginPage Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/login-page")

      // Assert
      titleMustContain("Verified login id")
    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      //Arrange / Act
      Formulate.loginPageDetails(browser)

      // Assert the title from the next page
      titleMustEqual("Login confirmation")
    }

  }

}