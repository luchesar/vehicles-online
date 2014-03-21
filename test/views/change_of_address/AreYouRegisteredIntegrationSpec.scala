package views.change_of_address

import helpers.webbrowser.TestHarness
import pages.change_of_address._
import helpers.UiSpec

class AreYouRegisteredIntegrationSpec extends UiSpec with TestHarness {

  "AreYouRegistered Integration" should {
    "be presented" in new WebBrowser {
      go to AreYouRegisteredPage

      assert(page.title equals AreYouRegisteredPage.title)
    }

    "go to next page after the button is clicked" in new WebBrowser {
      go to AreYouRegisteredPage

      click on AreYouRegisteredPage.signIn

      assert(page.title equals SignInProviderPage.title)
    }
  }
}