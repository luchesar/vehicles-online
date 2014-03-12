package views.disposal_of_vehicle

import org.specs2.mutable.Specification
import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness

class UprnNotFoundIntegrationSpec extends Specification with TestHarness  {

  "UprnNotFound Integration" should {
    "be presented" in new WebBrowser {
      // Arrange & Act
      go to UprnNotFoundPage

      // Assert
      assert(page.title equals UprnNotFoundPage.title)
    }

    "go to setuptradedetails page after the Setup Trade Details button is clicked" in new WebBrowser {
      // Arrange
      go to UprnNotFoundPage

      // Act
      click on UprnNotFoundPage.setupTradeDetails

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }

    "go to manualaddress page after the Manual Address button is clicked and trade details have been set up in cache" in new WebBrowser {
      // Arrange
      CacheSetup.setupTradeDetails()
      go to UprnNotFoundPage

      // Act
      click on UprnNotFoundPage.manualAddress

      // Assert
      assert(page.title equals EnterAddressManuallyPage.title)

    }

    "go to setuptradedetails page after the Manual Address button is clicked and trade details have not been set up in cache" in new WebBrowser {
      // Arrange
      go to UprnNotFoundPage

      // Act
      click on UprnNotFoundPage.manualAddress

      // Assert
      assert(page.title equals SetupTradeDetailsPage.title)
    }
  }
}