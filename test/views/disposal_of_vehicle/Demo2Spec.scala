package views.disposal_of_vehicle

import org.specs2.mutable.Specification
import pages.disposal_of_vehicle._
import helpers.TestHarness

class Demo2Spec extends Specification with TestHarness  {

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