package views.change_of_address

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.change_of_address.LoginPagePopulate
import helpers.change_of_address.AuthenticationPopulate
import helpers.change_of_address.V5cSearchPagePopulate
import helpers.UiSpec

class V5cSearchIntegrationSpec extends UiSpec {

  "V5cSearch Integration" should {
    "be presented when the login cache is complete" in new WithBrowser with BrowserMatchers {
      LoginPagePopulate.happyPath(browser)

      AuthenticationPopulate.happyPath(browser)
      browser.goTo("/v5c-search")

      titleMustEqual("Change of keeper address9")
    }

    "redirect to login when login cache is empty" in new WithBrowser with BrowserMatchers {
      browser.goTo("/v5c-search")

      titleMustEqual("Change of keeper address4")
    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      LoginPagePopulate.happyPath(browser)

      AuthenticationPopulate.happyPath(browser)

      V5cSearchPagePopulate.happyPath(browser)

      titleMustEqual("Change of keeper address10")
    }

    "reject submit when V5cReferenceNumber is blank" in new WithBrowser with BrowserMatchers {
      LoginPagePopulate.happyPath(browser)

      AuthenticationPopulate.happyPath(browser)

      V5cSearchPagePopulate.happyPath(browser, "")

      findMustEqualSize("div[class=validation-summary] ol li", 3)
    }

    "reject submit when V5cReferenceNumber contains less than minimum characters" in new WithBrowser with BrowserMatchers {
      LoginPagePopulate.happyPath(browser)

      AuthenticationPopulate.happyPath(browser)

      V5cSearchPagePopulate.happyPath(browser, "123")

      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    "reject submit when V5cReferenceNumber contains more than maximum characters" in new WithBrowser with BrowserMatchers {
      LoginPagePopulate.happyPath(browser)

      AuthenticationPopulate.happyPath(browser)

      V5cSearchPagePopulate.happyPath(browser, "12345678901234567890")

      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    "reject submit when V5cReferenceNumber contains letters" in new WithBrowser with BrowserMatchers {
      LoginPagePopulate.happyPath(browser)

      AuthenticationPopulate.happyPath(browser)

      V5cSearchPagePopulate.happyPath(browser, "qwertyuiopa")

      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }
  }
}