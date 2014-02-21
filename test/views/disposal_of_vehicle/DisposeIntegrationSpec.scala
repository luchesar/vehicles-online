package views.disposal_of_vehicle

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.{VehicleLookupPage, BusinessChooseYourAddressPage, DisposePage, SetUpTradeDetailsPage, DisposeConfirmationPage}
import mappings.disposal_of_vehicle.Dispose._

class DisposeIntegrationSpec extends Specification with Tags {
  "Dispose Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      browser.goTo(DisposePage.url)

      // Check the page title is correct
      titleMustEqual(DisposePage.title)
    }

    "display the next page when mandatory data is entered and dispose button is clicked" in new WithBrowser with BrowserMatchers {
      // Fill in mandatory data
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      DisposePage.happyPath(browser)

      // Verify we have moved to the next screen
      titleMustEqual(DisposeConfirmationPage.title)
    }

    "redirect when no traderBusinessName is cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo(DisposePage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "display validation errors when no fields are completed" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache

      DisposePage.happyPath(browser, day = "", month = "", year = "")

      // Assert
      checkNumberOfValidationErrors(1)
    }

    "display validation errors when month and year are input but no day" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache

      DisposePage.happyPath(browser,  day = "")

      // Assert
      checkNumberOfValidationErrors(1)
    }

    "display validation errors when day and year are input but no month" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache

      DisposePage.happyPath(browser,  month = "")

      // Assert
      checkNumberOfValidationErrors(1)
    }

    "display validation errors when day and month are input but no year" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache

      DisposePage.happyPath(browser,  year = "")

      // Assert
      checkNumberOfValidationErrors(1)
    }

    "display previous page when back link is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      BusinessChooseYourAddressPage.setupCache
      VehicleLookupPage.setupCache
      browser.goTo(DisposePage.url)
      browser.click("#backButton")

      // Assert
      titleMustEqual(VehicleLookupPage.title)
    }

  }
}
