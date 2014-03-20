package views.change_of_address

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.change_of_address.{AuthenticationPopulate, LoginPagePopulate}
import helpers.UiSpec

class AuthenticationIntegrationSpec extends UiSpec {

  "Authentication Integration" should {
    "be presented when we have a valid login in cache" in new WithBrowser with BrowserMatchers {
      LoginPagePopulate.happyPath(browser)

      browser.goTo("/authentication")

      titleMustEqual("Change of keeper address8")
    }
    "be redirected to login page when we do not have a valid login in cache" in new WithBrowser with BrowserMatchers {
      browser.goTo("/authentication")

      titleMustEqual("Change of keeper address4")
    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      LoginPagePopulate.happyPath(browser)
      AuthenticationPopulate.happyPath(browser)

      titleMustEqual("Change of keeper address9")
    }
  }
}

