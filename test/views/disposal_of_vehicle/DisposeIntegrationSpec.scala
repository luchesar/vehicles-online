package views.disposal_of_vehicle

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.{VehicleLookupPage, BusinessChooseYourAddressPage, DisposePage, SetUpTradeDetailsPage, DisposeSuccessPage}
import mappings.disposal_of_vehicle.Dispose._
import helpers.UiSpec

class DisposeIntegrationSpec extends UiSpec {
  "Dispose Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.setupVehicleDetailsModelCache()
      browser.goTo(DisposePage.url)

      titleMustEqual(DisposePage.title)
    }

    "redirect when no vehiclelookupformmodel is cached" in new WithBrowser with BrowserMatchers {
      // Fill in mandatory data
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.setupCache()
      browser.goTo(DisposePage.url)

      // Verify we have moved to the next screen
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "redirect when no traderBusinessName is cached" in new WithBrowser with BrowserMatchers {
      browser.goTo(DisposePage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "display validation errors when no fields are completed" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.setupVehicleDetailsModelCache()

      DisposePage.happyPath(browser, day = "", month = "", year = "")

      numberOfValidationErrorsMustEqual(1)
    }

    "display validation errors when month and year are input but no day" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.setupVehicleDetailsModelCache()

      DisposePage.happyPath(browser,  day = "")

      numberOfValidationErrorsMustEqual(1)
    }

    "display validation errors when day and year are input but no month" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.setupVehicleDetailsModelCache()

      DisposePage.happyPath(browser,  month = "")

      numberOfValidationErrorsMustEqual(1)
    }

    "display validation errors when day and month are input but no year" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.setupVehicleDetailsModelCache()

      DisposePage.happyPath(browser,  year = "")

      numberOfValidationErrorsMustEqual(1)
    }

    "display previous page when back link is clicked" in new WithBrowser with BrowserMatchers {
      BusinessChooseYourAddressPage.setupCache()
      VehicleLookupPage.setupVehicleDetailsModelCache()
      browser.goTo(DisposePage.url)
      browser.click("#backButton")

      titleMustEqual(VehicleLookupPage.title)
    }

    "display disposesuccess page on correct submission" in new WithBrowser with BrowserMatchers {
      DisposePage.happyPath(browser)

      browser.click("#submit")

      titleMustEqual(DisposeSuccessPage.title)
    }

    "display disposesuccess page on correct submission" in new WithBrowser with BrowserMatchers {
      DisposePage.happyPath(browser)

      browser.click("#submit")

      titleMustEqual(DisposeSuccessPage.title)
    }
  }
}
