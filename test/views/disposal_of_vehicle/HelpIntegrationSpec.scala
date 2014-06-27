package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.tags.UiTag
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle._
import pages.disposal_of_vehicle.HelpPage.exit

final class HelpIntegrationSpec extends UiSpec with TestHarness {
   "go to page" should {
     "display the page containing correct title" taggedAs UiTag in new WebBrowser {
       go to HelpPage

       page.title should equal(HelpPage.title)
     }
   }

  "exit" should {
    "leave the help page and display the start page" taggedAs UiTag in new WebBrowser {
      go to HelpPage

      click on exit

      page.title should equal(BeforeYouStartPage.title)
    }
  }
}
