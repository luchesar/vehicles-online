
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
      val validvehicleVRN = "A2"
      val validV5cReferenceNumber = "12345678910"

      // Arrange
      browser.goTo("/v5c-search")
      browser.fill("#V5cReferenceNumber") `with` validV5cReferenceNumber
      browser.fill("#VehicleRegistrationNumber") `with` validvehicleVRN

      // Act
      browser.submit("button[type='submit']")

      // Assert
      titleMustEqual("Change of keeper - confirm vehicle details")

  }

}
}