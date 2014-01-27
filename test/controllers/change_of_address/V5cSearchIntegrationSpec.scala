
package controllers.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class V5cSearchIntegrationSpec extends Specification with Tags {

  "V5cSearch Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/v5c-search")

      // Assert
      titleMustContain("retrieve a vehicle record")

    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      browser.goTo("/v5c-search")
      browser.fill("#V5cReferenceNumber") `with` "12345678910"
      browser.fill("#vehicleVRN") `with` "a1"

      // Act
      browser.submit("button[type='submit']")

      // Assert
      titleMustEqual("Change of keeper - confirm vehicle details") //TODO ensure valid VRN details are entered
    }

  }

}