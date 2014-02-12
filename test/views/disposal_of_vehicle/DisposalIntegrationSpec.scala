package views.disposal_of_vehicle

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import mappings.disposal_of_vehicle.Dispose._

class DisposalIntegrationSpec extends Specification with Tags {

  val disposeUrl = "/disposal-of-vehicle/dispose"

  "Disposal Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo(disposeUrl)

      // Check the page title is correct
      titleMustEqual("Dispose a vehicle into the motor trade: confirm")
    }

    "display the next page when mandatory data is entered and dispose button is clicked" in new WithBrowser with BrowserMatchers {

      // Fill in mandatory data
      browser.goTo(disposeUrl)

      // Do not click the consent checkbow as it already pre-populated
//      browser.click(s"#${consentId}")

      browser.click(s"#${dateOfDisposalId}_day option[value='1']")
      browser.click(s"#${dateOfDisposalId}_month option[value='1']")
      browser.fill(s"#${dateOfDisposalId}_year") `with` "2000"

      browser.submit("button[type='submit']")

      // Verify we have moved to the next screen
      titleMustEqual("Dispose a vehicle into the motor trade: summary")
    }
  }
}
