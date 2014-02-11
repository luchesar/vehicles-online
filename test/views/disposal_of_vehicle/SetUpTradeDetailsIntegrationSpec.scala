package views.disposal_of_vehicle

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.SetUpTradeDetailsHelper._

class SetUpTradeDetailsIntegrationSpec extends Specification with Tags {


  "BeforeYouStart Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/disposal-of-vehicle/setup-trade-details")

      // Assert
      titleMustContain("Dispose a vehicle into the motor trade")
    }

    "go to the next page when correct data is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser)

      // Assert
      titleMustEqual("Business: Choose your address")
    }

    "display five validation error messages when no details are entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderBusinessName = "", traderPostcode = "")
      checkNumberOfValidationErrors(5)

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade")
    }

    "display two validation error messages when a valid postcode is entered with no business name" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderBusinessName = "")
      checkNumberOfValidationErrors(2)

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade")
    }

    "display one validation error message when a valid postcode is entered with a business name less than min length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderBusinessName = "m")
      checkNumberOfValidationErrors(1)

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade")
    }

    "display one validation error message when a valid postcode is entered with a business name more than max length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderBusinessName = "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopq")
      checkNumberOfValidationErrors(1)

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade")
    }

    "display three validation error messages when a valid business name is entered with no postcode" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderPostcode = "")
      checkNumberOfValidationErrors(3)

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade")
    }

    "display two validation error messages when a valid business name is entered with a postcode less than min length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderPostcode = "a")
      checkNumberOfValidationErrors(2)

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade")
    }

    "display two validation error messages when a valid business name is entered with a postcode more than max length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderPostcode = "SA99 1DDD")
      checkNumberOfValidationErrors(2)

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade")
    }

    "display one validation error message when a valid business name is entered with a postcode containing an incorrect format" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      traderLookupIntegrationHelper(browser, traderPostcode = "SAR99")
      checkNumberOfValidationErrors(1)

      // Assert
      titleMustEqual("Dispose a vehicle into the motor trade")
    }
  }
}