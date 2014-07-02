package views.disposal_of_vehicle

import helpers.tags.UiTag
import pages.disposal_of_vehicle.EnterAddressManuallyPage._
import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.{TestGlobal, TestHarness}
import org.openqa.selenium.{By, WebElement, WebDriver}
import pages.common.ErrorPanel
import pages.disposal_of_vehicle._
import play.api.test.FakeApplication

final class EnterAddressManuallyIntegrationSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to EnterAddressManuallyPage

      page.title should equal(EnterAddressManuallyPage.title)
    }

    "display the progress of the page when progressBar is set to true" taggedAs UiTag in new WebBrowser(app = fakeApplicationWithProgressBarTrue) {
      go to BeforeYouStartPage
      cacheSetup()

      go to EnterAddressManuallyPage

      page.source.contains("Step 3 of 6") should equal(true)
    }

    "not display the progress of the page when progressBar is set to false" taggedAs UiTag in new WebBrowser(app = fakeApplicationWithProgressBarFalse) {
      go to BeforeYouStartPage
      cacheSetup()

      go to EnterAddressManuallyPage

      page.source.contains("Step 3 of 6") should equal(false)
    }

    "contain the hidden csrfToken field" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to EnterAddressManuallyPage
      val csrf: WebElement = webDriver.findElement(By.name(services.csrf_prevention.CsrfPreventionAction.TokenName))
      csrf.getAttribute("type") should equal("hidden")
      csrf.getAttribute("name") should equal(services.csrf_prevention.CsrfPreventionAction.TokenName)
      csrf.getAttribute("value").size > 0 should equal(true)
    }

    "not display certain labels when rendered with base template" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to EnterAddressManuallyPage

      page.source should not contain "addressAndPostcode"
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

  private val fakeApplicationWithProgressBarFalse = FakeApplication(
    withGlobal = Some(TestGlobal),
    additionalConfiguration = Map("progressBar.enabled" -> "false"))

  private val fakeApplicationWithProgressBarTrue = FakeApplication(
    withGlobal = Some(TestGlobal),
    additionalConfiguration = Map("progressBar.enabled" -> "true"))
}
