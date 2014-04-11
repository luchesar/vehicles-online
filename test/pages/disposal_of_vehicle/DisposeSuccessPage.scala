package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import mappings.disposal_of_vehicle.DisposeSuccess._

object DisposeSuccessPage extends Page with WebBrowserDSL {
  val address = "/disposal-of-vehicle/dispose-success"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "Dispose a vehicle into the motor trade: summary"

  def newDisposal(implicit driver: WebDriver): Element = find(id(newDisposalId)).get

  def exitDisposal(implicit driver: WebDriver): Element = find(id(exitDisposalId)).get

  def happyPath(implicit driver: WebDriver) = {
    go to DisposeSuccessPage
    click on DisposeSuccessPage.newDisposal
  }
}