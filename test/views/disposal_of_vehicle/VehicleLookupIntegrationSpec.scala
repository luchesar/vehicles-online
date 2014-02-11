package views.disposal_of_vehicle

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.VehicleLookup._

class VehicleLookupIntegrationSpec extends Specification with Tags {


  "VehicleLookupIntegrationSpec Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/disposal-of-vehicle/vehicle-lookup")

      // Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
    }

    "go to the next page when correct data is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser)

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade 5")
    }

    "display three validation error messages when no v5cReferenceNumber is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cReferenceNumber = "")

      // Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(3)
    }

    "display two validation error messages when no v5cRegistrationNumber is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cVehicleRegistrationNumber = "")

      //Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(2)
    }

    "display one validation error message when a v5cRegistrationNumber is entered containing one character" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cVehicleRegistrationNumber = "a")

      //Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(1)
    }

    "display one validation error message when a v5cRegistrationNumber is entered containing special characters" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cVehicleRegistrationNumber = "$^")

      //Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(1)
    }

    "display one validation error message when no v5cKeeperName is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cKeeperName = "")

      //Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(2)
    }

    "display one validation error message when a v5cKeeperName is entered which is greater than max length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cKeeperName = "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopq")

      //Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(1)
    }

    "display three validation error messages when no postcode is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cPostcode = "")

      //Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(3)
    }

    "display two validation error messages when a postcode less than min length is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cPostcode = "SA99")

      //Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(2)
    }

    "display one validation error message when a postcode containing a special character is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cPostcode = "SA991B%")

      //Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(1)
    }

    "display one validation error message when a postcode containing an incorrect format" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cPostcode = "SA9999")

      //Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(1)
    }

    "display ten validation error messages when no details are entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cReferenceNumber = "", v5cVehicleRegistrationNumber = "", v5cKeeperName = "", v5cPostcode = "")

      //Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(10)
    }

    "display seven validation error messages when only a valid v5cReferenceNumber is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cVehicleRegistrationNumber = "", v5cKeeperName = "", v5cPostcode = "")

      //Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(7)
    }

    "display eight validation error messages when only a valid v5cRegistrationNumber is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cReferenceNumber = "", v5cKeeperName = "", v5cPostcode = "")

      //Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(8)
    }

    "display eight validation error messages when only a valid v5cKeeperName is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cReferenceNumber = "", v5cVehicleRegistrationNumber = "", v5cPostcode = "")

      //Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(8)
    }

    "display seven validation error messages when only a valid v5cPostcode is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      vehicleLookupIntegrationHelper(browser, v5cReferenceNumber = "", v5cVehicleRegistrationNumber = "", v5cKeeperName = "")

      //Assert
      titleMustContain("Dispose a vehicle into the motor trade 4")
      checkNumberOfValidationErrors(7)
    }
  }
}