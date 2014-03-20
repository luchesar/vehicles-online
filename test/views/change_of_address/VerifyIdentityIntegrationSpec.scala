package views.change_of_address

import helpers.webbrowser.TestHarness
import pages.change_of_address._
import helpers.UiSpec

class VerifyIdentityIntegrationSpec extends UiSpec with TestHarness {

  "VerifyIdentity Integration" should {
    "be presented" in new WebBrowser {
      // Arrange & Act
      go to VerifyIdentityPage

      assert(page.title equals VerifyIdentityPage.title)
    }

    "go to next page after the button is clicked" in new WebBrowser {
      go to VerifyIdentityPage

      // Act
      click on VerifyIdentityPage.existingIdentityProfile

      assert(page.title equals AreYouRegisteredPage.title)
    }
  }
}