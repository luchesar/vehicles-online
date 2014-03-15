package views.change_of_address

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.UiSpec

class VerifyIdentityIntegrationSpec extends UiSpec {

  "VerifyIdentity Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/verify-identity")

      titleMustEqual("Change of keeper address3")
    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      browser.goTo("/verify-identity")

      browser.submit("button[type='submit']")

      titleMustEqual("Change of keeper address4")
    }
  }
}