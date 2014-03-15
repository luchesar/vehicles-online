package views.change_of_address

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.change_of_address.LoginPagePopulate._
import helpers.UiSpec

class LoginPageIntegrationSpec extends UiSpec {

  "LoginPage Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/login-page")

      titleMustEqual("Verified login id")
    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      happyPath(browser)

      titleMustEqual("Login confirmation")
    }
  }
}