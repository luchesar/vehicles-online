package views.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class AreYouRegisteredIntegrationSpec extends Specification with Tags {

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