package views.change_of_address

import org.specs2.mutable.Specification
import helpers.webbrowser.TestHarness
import pages.change_of_address._

class KeeperStatusIntegrationSpec extends Specification with TestHarness {

    "KeeperStatus Integration" should {
      "be presented" in new WebBrowser {
        // Arrange & Act
        go to KeeperStatusPage

        // Assert
        assert(page.title equals KeeperStatusPage.title)
      }

      "go to next page after the button is clicked" in new WebBrowser {
        // Arrange
        go to KeeperStatusPage

        // Act
        click on KeeperStatusPage.privateIndividual

        // Assert
        assert(page.title equals VerifyIdentityPage.title)
      }
    }
}


