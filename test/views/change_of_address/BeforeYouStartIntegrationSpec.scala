package views.change_of_address

import org.specs2.mutable.{Specification, Tags}
import play.api.test.WithBrowser
import controllers.BrowserMatchers

class BeforeYouStartIntegrationSpec extends Specification with Tags {

  "BeforeYouStart Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo("/before-you-start")

      // Assert
      titleMustEqual("Change of keeper address1")

    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      // Arrange
      browser.goTo("/before-you-start")

      // Act
      browser.submit("button[type='submit']")

      // Assert
      titleMustEqual("Change of keeper address2")
    }

  }

}