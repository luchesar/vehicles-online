package views.change_of_address

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.UiSpec

class BeforeYouStartIntegrationSpec extends UiSpec {

  "BeforeYouStart Integration" should {

    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo("/before-you-start")

      titleMustEqual("Change of keeper address1")
    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      browser.goTo("/before-you-start")

      browser.submit("button[type='submit']")

      titleMustEqual("Change of keeper address2")
    }
  }
}