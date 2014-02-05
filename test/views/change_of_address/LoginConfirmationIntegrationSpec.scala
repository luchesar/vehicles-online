package views.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.{TestHelper, BrowserMatchers}

class LoginConfirmationIntegrationSpec extends Specification with Tags {

  "LoginConfirmation Integration" should {

    "be presented when user login is cached" in new WithBrowser with BrowserMatchers {
      //Arrange / Act
      TestHelper.loginPagePopulate(browser)

      // Assert
      titleMustContain("Login confirmation")
    }

    "be presented when user login is not cached" in new WithBrowser with BrowserMatchers {
      //Arrange / Act
      browser.goTo("/login-confirmation")

      // Assert
      titleMustContain("Change of keeper address")
    }


    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      //Arrange
      TestHelper.loginPagePopulate(browser)

      // Act
      browser.submit("button[id='agree']")

      // Assert
      titleMustEqual("Change of keeper address")
    }
  }




}