package views.disposal_of_vehicle

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.BusinessChooseYourAddress._

class BusinessChooseYourAddressIntegrationSpec extends Specification with Tags {
  "business_choose_your_address Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/disposal-of-vehicle/business-choose-your-address")

      // Assert
      titleMustContain("Business: Choose your address")
    }

    "go to the next page when correct data is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      businessChooseYourAddressPopulate(browser)

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade 4")
    }

    "display two validation error messages when no businessName is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      businessChooseYourAddressPopulate(browser, businessName = "")

      // Assert
      checkNumberOfValidationErrors(2)
    }

    "display one validation error messages when businessName is longer than maxLength" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      businessChooseYourAddressPopulate(browser, businessName = "1234567890123456789012345678901234567890123456789012345678901234567890")

      //Assert
      checkNumberOfValidationErrors(1)
    }
  }
}