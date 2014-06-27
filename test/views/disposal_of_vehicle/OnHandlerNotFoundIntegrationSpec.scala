package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.tags.UiTag
import helpers.webbrowser.TestHarness
import pages.disposal_of_vehicle._

final class OnHandlerNotFoundIntegrationSpec extends UiSpec with TestHarness {
  "go to not found page" should {
    "display the page" taggedAs UiTag in new WebBrowser {
      go to OnHandlerNotFoundPage

      page.title should equal(OnHandlerNotFoundPage.title)
    }

  }
}