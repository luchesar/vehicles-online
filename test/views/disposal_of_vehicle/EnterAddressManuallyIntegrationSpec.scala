package views.disposal_of_vehicle

import helpers.tags.UiTag
import pages.disposal_of_vehicle.EnterAddressManuallyPage._
import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.TestHarness
import org.openqa.selenium.{By, WebElement, WebDriver}
import pages.common.ErrorPanel
import pages.disposal_of_vehicle._

final class EnterAddressManuallyIntegrationSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to EnterAddressManuallyPage

      page.title should equal(EnterAddressManuallyPage.title)
    }
    "contain the hidden csrfToken field" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to EnterAddressManuallyPage
      val csrf: WebElement = webDriver.findElement(By.name(services.csrf_prevention.CSRFAction.tokenName))
      csrf.getAttribute("type") should equal("hidden")
      csrf.getAttribute("name") should equal(services.csrf_prevention.CSRFAction.tokenName)
      csrf.getAttribute("value").size > 0 should equal(true)
    }
  }

  "next button" should {
    "accept and redirect when all fields are input with valid entry" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPath()

      page.title should equal(VehicleLookupPage.title)
    }

    "accept when only mandatory fields only are input" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPathMandatoryFieldsOnly()

      page.title should equal(VehicleLookupPage.title)
    }

    "display validation error messages when no details are entered" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      sadPath

      ErrorPanel.numberOfErrors should equal(2)
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) =
    CookieFactoryForUISpecs.setupTradeDetails()
}
