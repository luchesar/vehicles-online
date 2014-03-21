package views.change_of_address

import helpers.webbrowser.TestHarness
import pages.change_of_address._
import helpers.UiSpec

class SignInProviderIntegrationSpec extends UiSpec with TestHarness{

  "SignInProvider Integration" should {
    "be presented" in new WebBrowser {
      go to SignInProviderPage

      assert(page.title equals SignInProviderPage.title)
    }

    "go to next page after the button is clicked" in new WebBrowser {
      go to SignInProviderPage

      click on SignInProviderPage.postOffice

      assert(page.title equals LoginPage.title)
    }
  }
}