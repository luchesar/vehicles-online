package views.change_of_address

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.UiSpec

class KeeperStatusIntegrationSpec extends UiSpec {

  "KeeperStatus Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/keeper-status")

      titleMustEqual("Change of keeper address2")
    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      browser.goTo("/keeper-status")

      browser.submit("button[type='submit']")

      titleMustEqual("Change of keeper address3")
    }
  }
}


