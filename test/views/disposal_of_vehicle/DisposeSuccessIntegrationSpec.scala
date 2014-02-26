package views.disposal_of_vehicle

import org.specs2.mutable.{Tags, Specification}
import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle._

class DisposeSuccessIntegrationSpec extends Specification with Tags {
  "Dispose confirmation integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      DisposeFailurePage.cacheSetupHappyPath(browser)
      browser.goTo(DisposeSuccessPage.url)

      // Check the page title is correct
      titleMustEqual(DisposeSuccessPage.title)
    }

    "redirect when no details are cached" in new WithBrowser with BrowserMatchers {
      // Arrange & Act
      browser.goTo(DisposeSuccessPage.url)

      // Assert
      titleMustEqual(SetUpTradeDetailsPage.title)
    }

    "display vehicle lookup page when new disposal link is clicked" in new WithBrowser with BrowserMatchers {

      // Arrange
      DisposeFailurePage.cacheSetupHappyPath(browser)
      browser.goTo(DisposeSuccessPage.url)

      // Act
      browser.click("#newDisposal")

      //Assert
      titleMustEqual(VehicleLookupPage.title)
    }
  }
}
