package views.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.change_of_address.LoginPagePopulate._
import helpers.UiSpec

class LoginConfirmationIntegrationSpec extends UiSpec {

  "LoginConfirmation Integration" should {

    "be presented when user login is cached" in new WithBrowser with BrowserMatchers {
      happyPath(browser)

      titleMustEqual("Login confirmation")
    }

    "redirect when user login is not cached" in new WithBrowser with BrowserMatchers {
      browser.goTo("/login-confirmation")

      titleMustEqual("Change of keeper address4")
    }


    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      happyPath(browser)

      browser.submit("button[id='agree']")

      titleMustEqual("Change of keeper address8")
    }
  }
}