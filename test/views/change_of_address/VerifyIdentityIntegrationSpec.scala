package views.change_of_address

import org.specs2.mutable.Specification
import helpers.webbrowser.TestHarness
import pages.change_of_address._

class VerifyIdentityIntegrationSpec extends Specification with TestHarness {

  "VerifyIdentity Integration" should {
    "be presented" in new WebBrowser {
      // Arrange & Act
      go to VerifyIdentityPage

      // Assert
      assert(page.title equals VerifyIdentityPage.title)
    }

    "go to next page after the button is clicked" in new WebBrowser {
      // Arrange
      go to VerifyIdentityPage

      // Act
      click on VerifyIdentityPage.existingIdentityProfile

      // Assert
      assert(page.title equals AreYouRegisteredPage.title)
    }
  }
}