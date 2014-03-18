package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import helpers.UiSpec

class BeforeYouStartIntegrationSpec extends UiSpec with TestHarness  {

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
      assert(page.title equals SetupTradeDetailsPage.title)
    }
  }
}