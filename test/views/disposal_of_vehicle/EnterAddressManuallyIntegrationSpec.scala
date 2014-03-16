package views.disposal_of_vehicle

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.{SetUpTradeDetailsPage, EnterAddressManuallyPage}
import helpers.UiSpec

class EnterAddressManuallyIntegrationSpec extends UiSpec {

  "EnterAddressManually integration" should {

    "be presented" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()

      browser.goTo(EnterAddressManuallyPage.url)

      titleMustEqual(EnterAddressManuallyPage.title)
    }

    "accept when all fields are input" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()

      EnterAddressManuallyPage.happyPath(browser)

      numberOfValidationErrorsMustEqual(0)
    }

    "accept when only mandatory fields only are input" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()

      EnterAddressManuallyPage.happyPathMandatoryFieldsOnly(browser)

      numberOfValidationErrorsMustEqual(0)
    }

    "display validation error messages when no details are entered" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      browser.goTo(EnterAddressManuallyPage.url)

      browser.submit("button[type='submit']")

      numberOfValidationErrorsMustEqual(5)
    }

    "display validation error messages when a blank line 1 is entered" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.sadPath(browser, line1 = "")

      numberOfValidationErrorsMustEqual(2)
    }

    "display validation error messages when line 1 is entered which is greater than max length" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.happyPath(browser)
      EnterAddressManuallyPage.sadPath(browser, line1 = "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwerty")

      numberOfValidationErrorsMustEqual(1)
    }

    "display validation error messages when a blank postcode is entered" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.happyPath(browser)
      EnterAddressManuallyPage.sadPath(browser, postcode = "")

      numberOfValidationErrorsMustEqual(3)
    }

    "display validation error messages when a postcode is entered containing special characters" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.sadPath(browser, postcode = "SA99 1B!")

      numberOfValidationErrorsMustEqual(1)
    }

    "display validation error messages when a postcode is entered containing letters only" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.sadPath(browser, postcode = "ABCDE")

      numberOfValidationErrorsMustEqual(1)
    }

    "display validation error messages when a postcode is entered containing numbers only" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.sadPath(browser, postcode = "12345")

      numberOfValidationErrorsMustEqual(1)
    }

    "display validation error messages when a postcode is entered in an incorrect format" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.sadPath(browser, postcode = "SA99 1B1")

      numberOfValidationErrorsMustEqual(1)
    }

    "display validation error messages when a postcode is entered less than min length" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.sadPath(browser, postcode = "SA99")

      numberOfValidationErrorsMustEqual(2)
    }

    "display validation error messages when a postcode is entered greater than max legnth" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      EnterAddressManuallyPage.sadPath(browser, postcode = "SA99 1BDD")

      numberOfValidationErrorsMustEqual(2)
    }
  }
}
