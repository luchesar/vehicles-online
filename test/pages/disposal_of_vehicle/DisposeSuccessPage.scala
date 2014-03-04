package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.Config
import helpers.WebBrowser

// TODO Export this class as top-level class. This 'trait' is required as a result of a bug in ScalaTest.
// See https://github.com/scalatest/scalatest/issues/174
trait DisposeSuccessPage extends WebBrowser { this :WebBrowser =>

  object DisposeSuccessPage extends DisposeSuccessPage

  class DisposeSuccessPage extends Page {

    override val url: String = Config.baseUrl + "disposal-of-vehicle/dispose-success"
    override val title: String = ???

    def newDisposal(implicit driver: WebDriver): Element = find(id("newDisposal")).get

  }
}