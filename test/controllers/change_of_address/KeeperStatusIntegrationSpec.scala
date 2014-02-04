
package controllers.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class KeeperStatusIntegrationSpec extends Specification with Tags {

    "KeeperStatus Integration" should {
      "be presented" in new WithBrowser with BrowserMatchers {
        // Arrange & Act
        browser.goTo("/keeper-status")

        // Assert
        titleMustContain("Change of keeper address")
      }

      "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
        // Arrange
        browser.goTo("/keeper-status")

        // Act
        browser.submit("button[type='submit']")

        // Assert
        titleMustEqual("Change of keeper address")
      }
    }
}


