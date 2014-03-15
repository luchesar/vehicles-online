package views.disposal_of_vehicle

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.{EnterAddressManuallyPage, BusinessChooseYourAddressPage, SetUpTradeDetailsPage, VehicleLookupPage}
import java.util.concurrent.TimeUnit
import services.fakes.FakeAddressLookupService.postcodeInvalid
import helpers.UiSpec

class BusinessChooseYourAddressIntegrationSpec extends UiSpec {
  "Business choose your address - Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      browser.goTo(BusinessChooseYourAddressPage.url)

      titleMustEqual(BusinessChooseYourAddressPage.title)
    }

    "go to the next page when correct data is entered" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.happyPath(browser)

      titleMustEqual(VehicleLookupPage.title)
    }

    "go to the manual address entry page when manualAddressButton is clicked" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.happyPath(browser)
      browser.goTo(BusinessChooseYourAddressPage.url)
      browser.click("#enterAddressManuallyButton")

      titleMustEqual(EnterAddressManuallyPage.title)
    }

    "redirect when no traderBusinessName is cached" in new WithBrowser with BrowserMatchers {
      browser.goTo(BusinessChooseYourAddressPage.url)

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "display validation error messages when addressSelected is not in the list" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      BusinessChooseYourAddressPage.sadPath(browser)

      numberOfValidationErrorsMustEqual(1)
    }

    "display previous page when back link is clicked" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      browser.goTo(BusinessChooseYourAddressPage.url)

      browser.click("#backButton")

      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "go to the enter address manually page when the enter address manually link is clicked" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()
      browser.goTo(BusinessChooseYourAddressPage.url)

      browser.click("#enterAddressManuallyButton")

      titleMustEqual(EnterAddressManuallyPage.title)
    }

    "check number of options in drop down" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.happyPath(browser)
      browser.goTo(BusinessChooseYourAddressPage.url)

      val result = browser.find("#disposal_businessChooseYourAddress_addressSelect option").size

      result must beEqualTo(3) //this test currently looks at hardcoded service, however options within the drop down list are counted correctly
    }

    "display address dropdown when address service returns addresses" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache()

      browser.goTo(BusinessChooseYourAddressPage.url)

      browser.waitUntil[Boolean](duration, TimeUnit.SECONDS) {
        // TODO split this into two helpers, pass in the boolean on the first helper, pass in expected size on the second helper.
        browser.pageSource.contains("No addresses found for that postcode.") must beEqualTo(false) // No "not found" message present.

        val dropdownCount = browser.find("#disposal_businessChooseYourAddress_addressSelect").size
        dropdownCount mustEqual (1) // The dropdown is present.
      }
    }

    "display message when address service returns no addresses" in new WithBrowser with BrowserMatchers {
      SetUpTradeDetailsPage.setupCache(traderPostcode = postcodeInvalid)

      browser.goTo(BusinessChooseYourAddressPage.url)

      browser.waitUntil[Boolean](duration, TimeUnit.SECONDS) {
        browser.pageSource.contains("No addresses found for that postcode.") must beEqualTo(true) // "not found" message present.

        val dropdownCount = browser.find("#disposal_businessChooseYourAddress_addressSelect").size
        dropdownCount mustEqual (0) // The dropdown is not present.
      }
    }
  }
}