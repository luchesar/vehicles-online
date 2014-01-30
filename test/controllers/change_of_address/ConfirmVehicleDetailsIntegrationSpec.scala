
package controllers.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.{Formulate, BrowserMatchers}

class ConfirmVehicleDetailsIntegrationSpec extends Specification with Tags {

  "ConfirmVehicleDetails Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      //Arrange / Act
      Formulate.v5cSearchPageDetails(browser)

      // Assert
      titleMustContain("Change of keeper - confirm vehicle details")

    }
    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      //Arrange / Act
      Formulate.v5cSearchPageDetails(browser)

      // Act
      browser.submit("button[type='submit']")

      // Assert
      titleMustEqual("Change of keeper - confirm vehicle details")
    }
  }
}