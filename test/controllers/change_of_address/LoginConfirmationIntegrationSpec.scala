package controllers.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.{Formulate, BrowserMatchers}

class LoginConfirmationIntegrationSpec extends Specification with Tags {

  "LoginConfirmation Integration" should {

      "be presented" in new WithBrowser with BrowserMatchers {

       //Arrange / Act
        Formulate.loginPageDetails(browser)

        // Find the submit button on the login page and click it
        browser.submit("button[type='submit']")

        // Assert
        titleMustContain("Login confirmation")
      }


    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {

      //Arrange
      Formulate.loginPageDetails(browser)

      // Find the submit button on the login page and click it
      browser.submit("button[type='submit']")

      // Act
      browser.submit("button[id='agree']")

      // Assert
      titleMustEqual("Change of keeper - authentication")
    }
  }




}