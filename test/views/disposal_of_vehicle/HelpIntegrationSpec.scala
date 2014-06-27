package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.tags.UiTag
import helpers.webbrowser.{WebDriverFactory, TestHarness}
import pages.disposal_of_vehicle._
import pages.disposal_of_vehicle.HelpPage.{exit, back}
import org.openqa.selenium.WebDriver
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs


final class HelpIntegrationSpec extends UiSpec with TestHarness {
   "go to page" should {
     "display the page containing correct title" taggedAs UiTag in new WebBrowser {
       go to HelpPage

       page.title should equal(HelpPage.title)
     }
   }

  "back button" should {
    "redirect to the users previous page with javascript enabled " taggedAs UiTag in new WebBrowser(
        webDriver = WebDriverFactory.webDriver(targetBrowser = "htmlUnit", javascriptEnabled = true)) {

      go to BeforeYouStartPage
      cacheSetup()

      go to VehicleLookupPage

      go to HelpPage

      click on back

      page.title should equal(VehicleLookupPage.title)
    }

    "have no functionality with javascript disabled" taggedAs UiTag in new WebBrowser(
      webDriver = WebDriverFactory.webDriver(targetBrowser = "htmlUnit", javascriptEnabled = false)) {

      go to BeforeYouStartPage

      go to HelpPage

      click on back

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

  private def cacheSetup()(implicit webDriver: WebDriver) =
    CookieFactoryForUISpecs.
      setupTradeDetails().
      dealerDetails()
}
