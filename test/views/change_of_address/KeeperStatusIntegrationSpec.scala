package views.change_of_address

import helpers.webbrowser.TestHarness
import pages.change_of_address._
import helpers.UiSpec

class KeeperStatusIntegrationSpec extends UiSpec with TestHarness {

    "KeeperStatus Integration" should {
      "be presented" in new WebBrowser {
        // Arrange & Act
        go to KeeperStatusPage

          assert(page.title equals KeeperStatusPage.title)
      }

      "go to next page after the button is clicked" in new WebBrowser {
          go to KeeperStatusPage

        // Act
        click on KeeperStatusPage.privateIndividual

          assert(page.title equals VerifyIdentityPage.title)
      }
    }
  }


