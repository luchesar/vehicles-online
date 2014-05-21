package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import mappings.disposal_of_vehicle.DisposeSuccess._

object DisposeSuccessPage extends Page with WebBrowserDSL {
  final val address = "/disposal-of-vehicle/dispose-success"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  final override val title: String = "Dispose a vehicle into the motor trade: summary"

  def newDisposal(implicit driver: WebDriver): Element = find(id(NewDisposalId)).get

  def exitDisposal(implicit driver: WebDriver): Element = find(id(ExitDisposalId)).get

  def happyPath(implicit driver: WebDriver) = {
    go to DisposeSuccessPage
    click on DisposeSuccessPage.newDisposal
  }
}