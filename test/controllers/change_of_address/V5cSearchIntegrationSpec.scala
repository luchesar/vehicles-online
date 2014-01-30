
package controllers.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.{Formulate, BrowserMatchers}

class V5cSearchIntegrationSpec extends Specification with Tags {

  "V5cSearch Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/v5c-search")

      // Assert
      titleMustContain("retrieve a vehicle record")

    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {

      // Pass credentials through login page
      Formulate.loginPageDetails(browser)

      // Find the submit button on the login page and click it
      browser.submit("button[type='submit']")

      // Complete validation page by entering a pin
      browser.goTo("/authentication")
      browser.fill("#PIN") `with` "123456"
      browser.submit("button[type='submit']")

      // Complete V5c search page
       Formulate.v5cSearchPageDetails(browser)

      // Assert
      titleMustEqual("Change of keeper - confirm vehicle details")

  }

  }
}