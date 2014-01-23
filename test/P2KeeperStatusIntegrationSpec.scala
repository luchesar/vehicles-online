
import controllers.BrowserMatchers
import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser

class P2KeeperStatusIntegrationSpec extends Specification with Tags {

  "P2KeeperStatus" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/p2keeperstatus")

      // Assert
      titleMustContain("p2")
    }


    "go to next page on i'm a private individual button click" in new WithBrowser with BrowserMatchers {
      // Arrange
      browser.goTo("/")


      // Act
      browser.submit("input[type='submit']")

      // Assert
      titleMustEqual("Change of keeper")
    }



  }

}
