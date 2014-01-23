
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
        titleMustContain("Keeper status")

      }

      "go to next page on i'm a private individual button click" in new WithBrowser with BrowserMatchers {
        // Arrange
        browser.goTo("/keeper-status")

        // Act
        browser.submit("button[type='submit']")

        // Assert
        titleMustEqual("Change of keeper - verify identity") //TODO We need to change this to look at page3
      }



    }

}


