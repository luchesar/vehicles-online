package views.change_of_address

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.UiSpec

class AreYouRegisteredIntegrationSpec extends UiSpec {

  "AreYouRegistered Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/are-you-registered")

      titleMustEqual("Change of keeper address4")
    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {

      browser.goTo("/are-you-registered")

      browser.submit("button[type='submit']")

      titleMustEqual("Change of keeper address5")
    }
  }
}