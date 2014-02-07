package views.disposal_of_vehicle

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import controllers.TestHelper._

class VehicleLookupIntegrationSpec extends Specification with Tags {


  "BeforeYouStart Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/disposal-of-vehicle/vehicle-lookup")

      // Assert
      titleMustContain("Dispose a vehicle into the motor trade")
    }

    "go to the next page when correct data is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser)

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade") //TODO: This will need to check the next page - currently reloading same page
    }

    "display three validation error messages when no v5cReferenceNumber is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cReferenceNumber = "")

      // Assert
      titleMustContain("Dispose a vehicle into the motor trade")
      checkNumberOfValidationErrors(3)
    }

  }
}