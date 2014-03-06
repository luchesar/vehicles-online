package views.disposal_of_vehicle

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.{BusinessChooseYourAddressPage, VehicleLookupPage, SetUpTradeDetailsPage, DisposePage, EnterAddressManuallyPage}
import helpers.disposal_of_vehicle.Helper._

class VehicleLookupIntegrationSpec extends Specification with Tags {

  "VehicleLookupIntegrationSpec Integration" should {

    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache()
      browser.goTo(VehicleLookupPage.url)

      // Assert
      titleMustEqual(VehicleLookupPage.title)
    }

    "Redirect when no traderBusinessName is cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo(VehicleLookupPage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "go to the next page when correct data is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache()

      VehicleLookupPage.happyPath(browser)

      // Assert
      titleMustEqual(DisposePage.title)
    }

    "display three validation error messages when no referenceNumber is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.happyPath(browser, referenceNumber = "")

      // Assert
      checkNumberOfValidationErrors(3)
    }

    "display two validation error messages when no registrationNumber is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.happyPath(browser, vehicleRegistrationNumber = "")

      //Assert
      checkNumberOfValidationErrors(2)
    }

    "display one validation error message when a registrationNumber is entered containing one character" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.happyPath(browser, vehicleRegistrationNumber = "a")

      //Assert
      checkNumberOfValidationErrors(1)
    }

    "display one validation error message when a registrationNumber is entered containing special characters" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.happyPath(browser, vehicleRegistrationNumber = "$^")

      //Assert
      checkNumberOfValidationErrors(1)
    }

    "display five validation error messages when no details are entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.happyPath(browser, referenceNumber = "", vehicleRegistrationNumber = "")

      //Assert
      checkNumberOfValidationErrors(5)
    }

    "display two validation error messages when only a valid referenceNumber is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.happyPath(browser, vehicleRegistrationNumber = "")

      //Assert
      checkNumberOfValidationErrors(2)
    }

    "display three validation error messages when only a valid registrationNumber is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.happyPath(browser, referenceNumber = "")

      //Assert
      checkNumberOfValidationErrors(3)
    }

    "redirect when no dealerBusinessName is cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo(VehicleLookupPage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "display previous page when back link is clicked with uprn present" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache(addressWithUprn)
      browser.goTo(VehicleLookupPage.url)
      browser.click("#backButton")

      //Assert
      titleMustEqual(BusinessChooseYourAddressPage.title)
    }

    "display previous page when back link is clicked with no uprn present" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache()
      browser.goTo(VehicleLookupPage.url)
      browser.click("#backButton")

      //Assert
      titleMustEqual(EnterAddressManuallyPage.title)
    }
  }
}