package views.disposal_of_vehicle

import play.api.test.WithBrowser
import controllers.BrowserMatchers
import helpers.disposal_of_vehicle.{BeforeYouStartPage, SetUpTradeDetailsPage}
import helpers.UiSpec

class BeforeYouStartIntegrationSpec extends UiSpec {

  "BeforeYouStart Integration" should {
    "be presented" in new WithBrowser with BrowserMatchers {
      browser.goTo(BeforeYouStartPage.url)

      titleMustEqual(BeforeYouStartPage.title)
    }

    "go to next page after the button is clicked" in new WithBrowser with BrowserMatchers {
      browser.goTo(BeforeYouStartPage.url)

      browser.click("#next")

      titleMustEqual(SetUpTradeDetailsPage.title)
    }
  }
}