package views.change_of_address

import org.specs2.mutable.Specification
import helpers.webbrowser.TestHarness
import pages.change_of_address._

class SignInProviderIntegrationSpec extends Specification with TestHarness{

  "SignInProvider Integration" should {
    "be presented" in new WebBrowser {
      // Arrange & Act
      go to SignInProviderPage

      // Assert
      assert(page.title equals SignInProviderPage.title)
    }

    "go to next page after the button is clicked" in new WebBrowser {
      // Arrange
      go to SignInProviderPage

      // Act
      click on SignInProviderPage.postOffice

      // Assert
      assert(page.title equals LoginPage.title)
    }
  }
}