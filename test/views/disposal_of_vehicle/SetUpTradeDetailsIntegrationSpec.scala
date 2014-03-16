package views.disposal_of_vehicle

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.{SetUpTradeDetailsPage, BusinessChooseYourAddressPage}
import helpers.UiSpec

class SetUpTradeDetailsIntegrationSpec extends UiSpec {


  "SetUpTradeDetails Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo(SetUpTradeDetailsPage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "go to the next page when correct data is entered" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.happyPath(browser)

      titleMustEqual(BusinessChooseYourAddressPage.title)
    }

    "display five validation error messages when no details are entered" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.happyPath(browser, traderBusinessName = "", traderPostcode = "")

      numberOfValidationErrorsMustEqual(5)
    }

    "display two validation error messages when a valid postcode is entered with no business name" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.happyPath(browser, traderBusinessName = "")

      numberOfValidationErrorsMustEqual(2)
    }

    "display one validation error message when a valid postcode is entered with a business name less than min length" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.happyPath(browser, traderBusinessName = "m")

      numberOfValidationErrorsMustEqual(1)
    }

    "display one validation error message when a valid postcode is entered with a business name more than max length" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.happyPath(browser, traderBusinessName = "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopq")

      numberOfValidationErrorsMustEqual(1)
    }

    "display three validation error messages when a valid business name is entered with no postcode" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.happyPath(browser, traderPostcode = "")

      numberOfValidationErrorsMustEqual(3)
    }

    "display two validation error messages when a valid business name is entered with a postcode less than min length" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.happyPath(browser, traderPostcode = "a")

      numberOfValidationErrorsMustEqual(2)
    }

    "display two validation error messages when a valid business name is entered with a postcode more than max length" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.happyPath(browser, traderPostcode = "SA99 1DDD")

      numberOfValidationErrorsMustEqual(2)
    }

    "display one validation error message when a valid business name is entered with a postcode containing an incorrect format" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.happyPath(browser, traderPostcode = "SAR99")

      numberOfValidationErrorsMustEqual(1)
    }
  }
}