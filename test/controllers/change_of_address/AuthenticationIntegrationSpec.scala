package controllers.change_of_address

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.{Formulate, BrowserMatchers}

class AuthenticationIntegrationSpec extends Specification with Tags {

  "Authentication Integration" should {
    "be presented when we have a valid login in cache" in new WithBrowser with BrowserMatchers {
      //Arrange / Act
      Formulate.loginPageDetails(browser)

      // Find the submit button on the login page and click it
      browser.submit("button[type='submit']")

      browser.goTo("/authentication")

      // Assert
      titleMustContain("authentication")

    }
    "be redirected to logint page when we do not have a valid login in cache" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/authentication")

      // Assert
      titleMustContain("are you registered")

    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      //Arrange / Act
      Formulate.loginPageDetails(browser)

      // Find the submit button on the login page and click it
      browser.submit("button[type='submit']")

      browser.goTo("/authentication")

      browser.fill("#PIN") `with` "123456"
      browser.submit("button[type='submit']")

      // Assert
      titleMustEqual("Change of keeper - retrieve a vehicle record")
    }
  }
}

