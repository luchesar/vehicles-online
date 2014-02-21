package views.disposal_of_vehicle

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import org.specs2.mutable.{Tags, Specification}
import helpers.disposal_of_vehicle.{SetUpTradeDetailsPage, EnterAddressManuallyPage}

class EnterAddressManuallyIntegrationSpec extends Specification with Tags {

  "EnterAddressManually integration" should {

    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      browser.goTo(EnterAddressManuallyPage.url)

      // Assert
      titleMustEqual(EnterAddressManuallyPage.title)
    }

    "accept when all fields are input" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.happyPath(browser)

      // Assert
      checkNumberOfValidationErrors(0)
    }

    "accept when only mandatory fields only are input" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.happyPathMandatoryFieldsOnly(browser)

      // Assert
      checkNumberOfValidationErrors(0)
    }

    "display validation error messages when no details are entered" in new WithBrowser with BrowserMatchers {
      // Arrange
      SetUpTradeDetailsPage.setupCache()
      browser.goTo("/disposal-of-vehicle/enter-address-manually")

      // Act
      browser.submit("button[type='submit']")

      // Assert
      checkNumberOfValidationErrors(4)
    }

    "display validation error messages when a blank line 1 is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.sadPath(browser, line1 = "")

      // Assert
      checkNumberOfValidationErrors(1)
    }

    "display validation error messages when line 1 is entered which is greater than max length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.happyPath(browser)
      EnterAddressManuallyPage.sadPath(browser, line1 = "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwerty")

      // Assert
      checkNumberOfValidationErrors(1)
    }

    "display validation error messages when a blank postcode is entered" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.happyPath(browser)
      EnterAddressManuallyPage.sadPath(browser, postcode = "")

      // Assert
      checkNumberOfValidationErrors(3)
    }

    "display validation error messages when a postcode is entered containing special characters" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.sadPath(browser, postcode = "SA99 1B!")

      // Assert
      checkNumberOfValidationErrors(1)
    }

    "display validation error messages when a postcode is entered containing letters only" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.sadPath(browser, postcode = "ABCDE")

      // Assert
      checkNumberOfValidationErrors(1)
    }

    "display validation error messages when a postcode is entered containing numbers only" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.sadPath(browser, postcode = "12345")

      // Assert
      checkNumberOfValidationErrors(1)
    }

    "display validation error messages when a postcode is entered in an incorrect format" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.sadPath(browser, postcode = "SA99 1B1")

      // Assert
      checkNumberOfValidationErrors(1)
    }

    "display validation error messages when a postcode is entered less than min length" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.sadPath(browser, postcode = "SA99")

      // Assert
      checkNumberOfValidationErrors(2)
    }

    "display validation error messages when a postcode is entered greater than max legnth" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.sadPath(browser, postcode = "SA99 1BDD")

      // Assert
      checkNumberOfValidationErrors(2)
    }
  }
}
