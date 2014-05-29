package views.disposal_of_vehicle

import pages.disposal_of_vehicle.EnterAddressManuallyPage._
import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.TestHarness
import org.openqa.selenium.WebDriver
import pages.common.ErrorPanel
import pages.disposal_of_vehicle._

final class EnterAddressManuallyIntegrationSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to EnterAddressManuallyPage

      page.title should equal(EnterAddressManuallyPage.title)
    }
  }

  "next button" should {
    "accept and redirect when all fields are input with valid entry" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPath()

      page.title should equal(VehicleLookupPage.title)
    }

    "accept when only mandatory fields only are input" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPathMandatoryFieldsOnly()

      page.title should equal(VehicleLookupPage.title)
    }

    "display validation error messages when no details are entered" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      sadPath

      ErrorPanel.numberOfErrors should equal(5)
    }

    "display validation error messages when a blank postcode is entered" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPath(postcode = "")

      ErrorPanel.numberOfErrors should equal(3)
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) =
    CookieFactoryForUISpecs.setupTradeDetails()
}
