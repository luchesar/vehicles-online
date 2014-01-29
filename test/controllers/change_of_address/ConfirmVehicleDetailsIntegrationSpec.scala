
package controllers.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class ConfirmVehicleDetailsIntegrationSpec extends Specification with Tags {

  "ConfirmVehicleDetails Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act - Go to previous page, fill textboxes before submit button is pressed
      val validvehicleVRN = "A2"
      val validV5cReferenceNumber = "12345678910"
      browser.goTo("/v5c-search")
      browser.fill("#V5cReferenceNumber") `with` validV5cReferenceNumber
      browser.fill("#VehicleRegistrationNumber") `with` validvehicleVRN
      browser.submit("button[type='submit']")

      // Assert
      titleMustContain("Change of keeper - confirm vehicle details")

    }
    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      val validvehicleVRN = "A2"
      val validV5cReferenceNumber = "12345678910"
      browser.goTo("/v5c-search")
      browser.fill("#V5cReferenceNumber") `with` validV5cReferenceNumber
      browser.fill("#VehicleRegistrationNumber") `with` validvehicleVRN
      browser.submit("button[type='submit']")

      // Act
      browser.submit("button[type='submit']")

      // Assert
      titleMustEqual("Change of keeper - confirm vehicle details")
    }
  }
}