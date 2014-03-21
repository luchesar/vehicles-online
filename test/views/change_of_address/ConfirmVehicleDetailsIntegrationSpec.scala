package views.change_of_address

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.change_of_address.LoginPagePopulate
import helpers.change_of_address.AuthenticationPopulate
import helpers.change_of_address.V5cSearchPagePopulate
import helpers.UiSpec

class ConfirmVehicleDetailsIntegrationSpec extends UiSpec {

  "ConfirmVehicleDetails Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      LoginPagePopulate.happyPath(browser)

      AuthenticationPopulate.happyPath(browser)

      V5cSearchPagePopulate.happyPath(browser)

      titleMustEqual("Change of keeper address10")
    }


    "redirect to login when login cache is empty" in new WithBrowser with BrowserMatchers {
      browser.goTo("/confirm-vehicle-details")

      titleMustEqual("Change of keeper address4")
    }

    "v5c search page is presented when user is logged in but not entered vehicle details" in new WithBrowser with BrowserMatchers {
      LoginPagePopulate.happyPath(browser)

      AuthenticationPopulate.happyPath(browser)

      browser.goTo("/confirm-vehicle-details")

      titleMustEqual("Change of keeper address9")
    }


    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      LoginPagePopulate.happyPath(browser)

      AuthenticationPopulate.happyPath(browser)

      V5cSearchPagePopulate.happyPath(browser)

      titleMustEqual("Change of keeper address10")
    }
  }
}