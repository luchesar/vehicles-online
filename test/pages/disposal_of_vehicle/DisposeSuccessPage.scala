package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object DisposeSuccessPage extends Page with WebBrowserDSL {

  override val url: String = WebDriverFactory.testUrl + "disposal-of-vehicle/dispose-success"
  override val title: String = "Dispose a vehicle into the motor trade: summary"

  def newDisposal(implicit driver: WebDriver): Element = find(id("newDisposal")).get

  def happyPath(implicit driver: WebDriver) = {
    CacheSetup.businessChooseYourAddress()
    CacheSetup.vehicleDetailsModel()
    CacheSetup.disposeFormModel()
    CacheSetup.disposeTransactionId()
    CacheSetup.vehicleRegistrationNumber()
    go to DisposeSuccessPage.url

    click on DisposeSuccessPage.newDisposal
  }
}