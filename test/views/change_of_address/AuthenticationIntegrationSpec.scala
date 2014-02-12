package views.change_of_address

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.change_of_address.{AuthenticationPopulate, LoginPagePopulate}

class AuthenticationIntegrationSpec extends Specification with Tags {

  "Authentication Integration" should {
    "be presented when we have a valid login in cache" in new WithBrowser with BrowserMatchers {
      //Arrange / Act
      LoginPagePopulate.happyPath(browser)

      browser.goTo("/authentication")

      // Assert
      titleMustEqual("Change of keeper address8")
    }
    "be redirected to login page when we do not have a valid login in cache" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/authentication")

      // Assert
      titleMustEqual("Change of keeper address4")
    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      //Arrange / Act
      LoginPagePopulate.happyPath(browser)
      AuthenticationPopulate.happyPath(browser)

      // Assert
      titleMustEqual("Change of keeper address9")
    }
  }
}

