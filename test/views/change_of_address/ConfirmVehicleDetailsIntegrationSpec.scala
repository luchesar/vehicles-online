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
      // Pass credentials through login page
      LoginPagePopulate.happyPath(browser)

      // Complete validation page by entering a pin
      AuthenticationPopulate.happyPath(browser)

      // Complete V5c search page with vehicle details
      V5cSearchPagePopulate.happyPath(browser)

      titleMustEqual("Change of keeper address10")
    }


    "redirect to login when login cache is empty" in new WithBrowser with BrowserMatchers {
      browser.goTo("/confirm-vehicle-details")

      titleMustEqual("Change of keeper address4")
    }

    "v5c search page is presented when user is logged in but not entered vehicle details" in new WithBrowser with BrowserMatchers {
      // Pass credentials through login page
      LoginPagePopulate.happyPath(browser)

      // Complete validation page by entering a pin
      AuthenticationPopulate.happyPath(browser)

      // Try to access confirm vehicle details page without entering V5c details
      browser.goTo("/confirm-vehicle-details")

      titleMustEqual("Change of keeper address9")
    }


    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      // Pass credentials through login page and click submit
      LoginPagePopulate.happyPath(browser)

      // Complete validation page by entering a pin
      AuthenticationPopulate.happyPath(browser)

      // Complete V5c search page
      V5cSearchPagePopulate.happyPath(browser)

      titleMustEqual("Change of keeper address10")
    }
  }
}