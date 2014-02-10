package views.disposal_of_vehicle

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import controllers.TestHelper._

class BusinessChooseYourAddressIntegrationSpec extends Specification with Tags {
  "businessChooseYourAddress Integration" should {
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
      titleMustEqual("Dispose a vehicle into the motor trade") //TODO: This will need to check the next page - currently reloading same page
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
pending("Need to copy code from valtech_views that deals with maxLength in test mode")
      //Assert
      checkNumberOfValidationErrors(2)
    }
  }
}