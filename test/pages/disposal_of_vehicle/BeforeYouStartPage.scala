package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import pages.BasePage
import helpers.Config
import helpers.WebBrowser

// TODO Export this class as top-level class. This 'trait' is required as a result of a bug in ScalaTest.
// See https://github.com/scalatest/scalatest/issues/174
trait BeforeYouStartPage extends BasePage { this :WebBrowser =>

  object BeforeYouStartPage extends BeforeYouStartPage

  class BeforeYouStartPage extends Page {

    override val url: String = Config.baseUrl

    def startNow(implicit driver: WebDriver): Element = find(id("next")).get

  }
}