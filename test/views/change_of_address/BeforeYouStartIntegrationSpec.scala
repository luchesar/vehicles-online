package views.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.webbrowser.TestHarness
import pages.change_of_address._

class BeforeYouStartIntegrationSpec extends Specification with TestHarness {

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
      assert(page.title equals KeeperStatus.title)
    }
  }
}