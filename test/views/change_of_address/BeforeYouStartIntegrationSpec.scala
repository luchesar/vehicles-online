package views.change_of_address

import helpers.UiSpec
import org.specs2.mutable.Specification
import helpers.webbrowser.TestHarness
import pages.change_of_address._

class BeforeYouStartIntegrationSpec extends UiSpec with TestHarness {

  "BeforeYouStart Integration" should {
    "be presented" in new WebBrowser {
      // Arrange & Act
      go to BeforeYouStartPage

      // Assert
      assert(page.title equals BeforeYouStartPage.title)
    }

    "go to next page after the button is clicked" in new WebBrowser {
      // Arrange
      go to BeforeYouStartPage

      // Act
      click on BeforeYouStartPage.startNow

      // Assert
      assert(page.title equals KeeperStatusPage.title)
    }
  }
}