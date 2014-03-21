package views.disposal_of_vehicle

import pages.disposal_of_vehicle._
import helpers.webbrowser.TestHarness
import helpers.UiSpec

class BeforeYouStartIntegrationSpec extends UiSpec with TestHarness  {

  "BeforeYouStart Integration" should {
    "be presented" in new WebBrowser {
      go to BeforeYouStartPage

      assert(page.title equals BeforeYouStartPage.title)
    }

    "go to next page after the button is clicked" in new WebBrowser {
      go to BeforeYouStartPage

      click on BeforeYouStartPage.startNow

      assert(page.title equals SetupTradeDetailsPage.title)
    }
  }
}