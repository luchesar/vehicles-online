package views.disposal_of_vehicle

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.{BusinessChooseYourAddressPage, VehicleLookupPage, SetUpTradeDetailsPage, DisposePage, EnterAddressManuallyPage}
import helpers.disposal_of_vehicle.Helper._
import helpers.UiSpec

class VehicleLookupIntegrationSpec extends UiSpec {
  "VehicleLookupIntegrationSpec Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      browser.goTo(VehicleLookupPage.url)

      titleMustEqual(VehicleLookupPage.title)
    }

    "Redirect when no traderBusinessName is cached" in new WithBrowser with BrowserMatchers {
      browser.goTo(VehicleLookupPage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "go to the next page when correct data is entered" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()

      VehicleLookupPage.happyPath(browser)

      titleMustEqual(DisposePage.title)
    }

    "display three validation error messages when no referenceNumber is entered" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.happyPath(browser, referenceNumber = "")

      numberOfValidationErrorsMustEqual(3)
    }

    "display two validation error messages when no registrationNumber is entered" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.happyPath(browser, vehicleRegistrationNumber = "")

      numberOfValidationErrorsMustEqual(2)
    }

    "display one validation error message when a registrationNumber is entered containing one character" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.happyPath(browser, vehicleRegistrationNumber = "a")

      numberOfValidationErrorsMustEqual(2)
    }

    "display one validation error message when a registrationNumber is entered containing special characters" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.happyPath(browser, vehicleRegistrationNumber = "$^")

      numberOfValidationErrorsMustEqual(1)
    }

    "display five validation error messages when no details are entered" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.happyPath(browser, referenceNumber = "", vehicleRegistrationNumber = "")

      numberOfValidationErrorsMustEqual(5)
    }

    "display two validation error messages when only a valid referenceNumber is entered" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.happyPath(browser, vehicleRegistrationNumber = "")

      numberOfValidationErrorsMustEqual(2)
    }

    "display three validation error messages when only a valid registrationNumber is entered" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.happyPath(browser, referenceNumber = "")

      numberOfValidationErrorsMustEqual(3)
    }

    "redirect when no dealerBusinessName is cached" in new WithBrowser with BrowserMatchers {
      browser.goTo(VehicleLookupPage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "display previous page when back link is clicked with uprn present" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache(addressWithUprn)
      browser.goTo(VehicleLookupPage.url)
      browser.click("#backButton")

      titleMustEqual(BusinessChooseYourAddressPage.title)
    }

    "display previous page when back link is clicked with no uprn present" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache()
      browser.goTo(VehicleLookupPage.url)
      browser.click("#backButton")

      titleMustEqual(EnterAddressManuallyPage.title)
    }
  }
}