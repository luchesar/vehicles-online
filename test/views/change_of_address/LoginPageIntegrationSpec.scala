package views.change_of_address

import org.specs2.mutable.Specification
import helpers.webbrowser.TestHarness
import pages.change_of_address._
import helpers.UiSpec

class LoginPageIntegrationSpec extends UiSpec with TestHarness {

  "LoginPage Integration" should {
    "be presented" in new WebBrowser {
      // Arrange & Act
      go to LoginPage

      assert(page.title equals LoginPage.title)
    }
    "go to next page after the button is clicked" in new WebBrowser {
      //Arrange / Act
      //happyPath(browser)

      LoginPage.happyPath

      // Assert the title from the next page
      //assert(page.title equals LoginPage.title)
    }
  }
}