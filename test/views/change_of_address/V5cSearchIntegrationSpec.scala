package views.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.change_of_address.LoginPagePopulate
import helpers.change_of_address.AuthenticationPopulate
import helpers.change_of_address.V5cSearchPagePopulate

class V5cSearchIntegrationSpec extends Specification with Tags {

  "V5cSearch Integration" should {
    "be presented when the login cache is complete" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      // Pass credentials through login page and click submit
      LoginPagePopulate.happyPath(browser)

      // Complete validation page by entering a pin
      AuthenticationPopulate.happyPath(browser)
      browser.goTo("/v5c-search")

      // Assert
      titleMustEqual("Change of keeper address9")
    }

    "redirect to login when login cache is empty" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/v5c-search")

      // Assert
      titleMustEqual("Change of keeper address4")

    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      //Arrange & Act
      // Pass credentials through login page
      LoginPagePopulate.happyPath(browser)

      // Complete validation page by entering a pin
      AuthenticationPopulate.happyPath(browser)

      // Complete V5c search page
      V5cSearchPagePopulate.happyPath(browser)

      // Assert
      titleMustEqual("Change of keeper address10")
    }

    "reject submit when V5cReferenceNumber is blank" in new WithBrowser with BrowserMatchers {
      //Arrange & Act
      // Pass credentials through login page
      LoginPagePopulate.happyPath(browser)

      // Complete validation page by entering a pin
      AuthenticationPopulate.happyPath(browser)

      // Complete V5c search page
      V5cSearchPagePopulate.happyPath(browser, "")

      // Assert
      findMustEqualSize("div[class=validation-summary] ol li", 3)
    }

    "reject submit when V5cReferenceNumber contains less than minimum characters" in new WithBrowser with BrowserMatchers {
      //Arrange & Act
      // Pass credentials through login page
      LoginPagePopulate.happyPath(browser)

      // Complete validation page by entering a pin
      AuthenticationPopulate.happyPath(browser)

      // Complete V5c search page
      V5cSearchPagePopulate.happyPath(browser, "123")

      // Assert
      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    "reject submit when V5cReferenceNumber contains more than maximum characters" in new WithBrowser with BrowserMatchers {
      //Arrange & Act
      // Pass credentials through login page
      LoginPagePopulate.happyPath(browser)

      // Complete validation page by entering a pin
      AuthenticationPopulate.happyPath(browser)

      // Complete V5c search page
      V5cSearchPagePopulate.happyPath(browser, "12345678901234567890")

      // Assert
      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    "reject submit when V5cReferenceNumber contains letters" in new WithBrowser with BrowserMatchers {
      //Arrange & Act
      // Pass credentials through login page
      LoginPagePopulate.happyPath(browser)

      // Complete validation page by entering a pin
      AuthenticationPopulate.happyPath(browser)

      // Complete V5c search page
      V5cSearchPagePopulate.happyPath(browser, "qwertyuiopa")

      // Assert
      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

  }
}