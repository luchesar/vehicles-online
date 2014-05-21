package views.disposal_of_vehicle

import pages.disposal_of_vehicle.EnterAddressManuallyPage._
import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.TestHarness
import org.openqa.selenium.WebDriver
import pages.common.ErrorPanel
import pages.disposal_of_vehicle._

final class EnterAddressManuallyIntegrationSpec extends UiSpec with TestHarness {

  "EnterAddressManually integration" should {

    "be presented" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to EnterAddressManuallyPage

      assert(page.title equals EnterAddressManuallyPage.title)
    }

    "accept and redirect when all fields are input with valid entry" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPath()

      assert(page.title equals VehicleLookupPage.title)
    }

    "accept when only mandatory fields only are input" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPathMandatoryFieldsOnly()

      assert(page.title equals VehicleLookupPage.title)
    }

    "display validation error messages when no details are entered" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      sadPath

      assert(ErrorPanel.numberOfErrors equals 5)
    }

    "display validation error messages when a blank postcode is entered" in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPath(postcode = "")

      assert(ErrorPanel.numberOfErrors equals 3)
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) =
    CookieFactoryForUISpecs.setupTradeDetailsIntegration()
}
