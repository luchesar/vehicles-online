package views.disposal_of_vehicle

import helpers.tags.UiTag
import pages.disposal_of_vehicle.VehicleLookupPage._
import helpers.UiSpec
import helpers.disposal_of_vehicle.CookieFactoryForUISpecs
import helpers.webbrowser.TestHarness
import org.openqa.selenium.{By, WebElement, WebDriver}
import pages.common.ErrorPanel
import pages.disposal_of_vehicle._
import services.fakes.FakeAddressLookupService._

final class VehicleLookupIntegrationSpec extends UiSpec with TestHarness {
  "go to page" should {
    "display the page" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      go to VehicleLookupPage

      page.title should equal(VehicleLookupPage.title)
    }

    "Redirect when no traderBusinessName is cached" taggedAs UiTag in new WebBrowser {
      go to VehicleLookupPage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "redirect when no dealerBusinessName is cached" taggedAs UiTag in new WebBrowser {
      go to VehicleLookupPage

      page.title should equal(SetupTradeDetailsPage.title)
    }

    "contain the hidden csrfToken field" taggedAs UiTag in new WebBrowser {
      go to VehicleLookupPage
      val csrf: WebElement = webDriver.findElement(By.name(services.csrf_prevention.CSRFPreventionAction.csrfPreventionTokenName))
      csrf.getAttribute("type") should equal("hidden")
      csrf.getAttribute("name") should equal(services.csrf_prevention.CSRFPreventionAction.csrfPreventionTokenName)
      csrf.getAttribute("value").size > 0 should equal(true)
    }
  }

  "findVehicleDetails button" should {
    "go to the next page when correct data is entered" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPath()

      page.title should equal(DisposePage.title)
    }

    "display one validation error message when no referenceNumber is entered" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPath(referenceNumber = "")

      ErrorPanel.numberOfErrors should equal(1)
    }

    "display one validation error message when no registrationNumber is entered" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPath(registrationNumber = "")

      ErrorPanel.numberOfErrors should equal(1)
    }

    "display one validation error message when a registrationNumber is entered containing one character" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPath(registrationNumber = "a")

      ErrorPanel.numberOfErrors should equal(1)
    }

    "display one validation error message when a registrationNumber is entered containing special characters" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPath(registrationNumber = "$^")

      ErrorPanel.numberOfErrors should equal(1)
    }

    "display two validation error messages when no vehicle details are entered but consent is given" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPath(referenceNumber = "", registrationNumber = "")

      ErrorPanel.numberOfErrors should equal(2)
    }

    "display one validation error message when only a valid referenceNumber is entered and consent is given" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPath(registrationNumber = "")

      ErrorPanel.numberOfErrors should equal(1)
    }

    "display one validation error message when only a valid registrationNumber is entered and consent is given" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      happyPath(referenceNumber = "")

      ErrorPanel.numberOfErrors should equal(1)
    }

    "redirect to vrm locked when too many attempting to lookup a locked vrm" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()

      tryLockedVrm()
      page.title should equal(VrmLockedPage.title)
    }
  }

  "back" should {
    "display previous page when back link is clicked with uprn present" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      CookieFactoryForUISpecs.
        setupTradeDetails().
        dealerDetails(addressWithUprn)
      go to VehicleLookupPage

      click on back

      page.title should equal(BusinessChooseYourAddressPage.title)
    }

    "display previous page when back link is clicked with no uprn present" taggedAs UiTag in new WebBrowser {
      go to BeforeYouStartPage
      cacheSetup()
      go to VehicleLookupPage

      click on back

      page.title should equal(EnterAddressManuallyPage.title)
    }
  }

  private def cacheSetup()(implicit webDriver: WebDriver) =
    CookieFactoryForUISpecs.
      setupTradeDetails().
      dealerDetails()
}
