package views.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.change_of_address.LoginPagePopulate._
import helpers.change_of_address.V5cSearchPagePopulate
import V5cSearchPagePopulate._
import helpers.change_of_address.AuthenticationPopulate._

class V5cSearchIntegrationSpec extends Specification with Tags {

  "V5cSearch Integration" should {
    "be presented when the login cache is complete" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      // Pass credentials through login page and click submit
      loginPagePopulate(browser)

      // Complete validation page by entering a pin
      browser.goTo("/authentication")
      browser.fill("#PIN") `with` "123456"
      browser.submit("button[type='submit']")
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
      loginPagePopulate(browser)

      // Complete validation page by entering a pin
      browser.goTo("/authentication")
      browser.fill("#PIN") `with` "123456"
      browser.submit("button[type='submit']")

      // Complete V5c search page
      v5cSearchPagePopulate(browser)

      // Assert
      titleMustEqual("Change of keeper address10")
    }

    "reject submit when V5cReferenceNumber is blank" in new WithBrowser with BrowserMatchers {
      //Arrange & Act
      // Pass credentials through login page
      loginPagePopulate(browser)

      // Complete validation page by entering a pin
      authenticationPopulate(browser)

      // Complete V5c search page
      v5cSearchPagePopulate(browser, "")

      // Assert
      findMustEqualSize("div[class=validation-summary] ol li", 3)
    }

    "reject submit when V5cReferenceNumber contains less than minimum characters" in new WithBrowser with BrowserMatchers {
      //Arrange & Act
      // Pass credentials through login page
      loginPagePopulate(browser)

      // Complete validation page by entering a pin
      authenticationPopulate(browser)

      // Complete V5c search page
      v5cSearchPagePopulate(browser, "123")

      // Assert
      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    "reject submit when V5cReferenceNumber contains more than maximum characters" in new WithBrowser with BrowserMatchers {
      //Arrange & Act
      // Pass credentials through login page
      loginPagePopulate(browser)

      // Complete validation page by entering a pin
      authenticationPopulate(browser)

      // Complete V5c search page
      v5cSearchPagePopulate(browser, "12345678901234567890")

      // Assert
      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

    "reject submit when V5cReferenceNumber contains letters" in new WithBrowser with BrowserMatchers {
      //Arrange & Act
      // Pass credentials through login page
      loginPagePopulate(browser)

      // Complete validation page by entering a pin
      authenticationPopulate(browser)

      // Complete V5c search page
      v5cSearchPagePopulate(browser, "qwertyuiopa")

      // Assert
      findMustEqualSize("div[class=validation-summary] ol li", 1)
    }

  }
}