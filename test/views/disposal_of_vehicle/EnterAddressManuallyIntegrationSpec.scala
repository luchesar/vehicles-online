package views.disposal_of_vehicle

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import org.specs2.mutable.{Tags, Specification}
import helpers.disposal_of_vehicle.EnterAddressManuallyPage

class EnterAddressManuallyIntegrationSpec extends Specification with Tags {

  "EnterAddressManually integration" should {

    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo(EnterAddressManuallyPage.url)

      // Assert
      titleMustEqual(EnterAddressManuallyPage.title)
    }

    "accept when all fields are input" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      EnterAddressManuallyPage.happyPath(browser)

      // Assert
      checkNumberOfValidationErrors(0)
    }

    "accept when only mandatory fields only are input" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      EnterAddressManuallyPage.happyPathMandatoryFieldsOnly(browser)

      // Assert
      checkNumberOfValidationErrors(0)
    }

    "display expected number of validation error messages when no details are entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/disposal-of-vehicle/enter-address-manually")

      // Act
      browser.submit("button[type='submit']")

      // Assert
      checkNumberOfValidationErrors(4)
    }


    "reject when a blank postcode is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      EnterAddressManuallyPage.happyPath(browser, postcode = "")

      // Assert
      checkNumberOfValidationErrors(3)
    }
  }
}
