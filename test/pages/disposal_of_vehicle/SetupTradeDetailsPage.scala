package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import services.fakes.FakeAddressLookupService._
import mappings.disposal_of_vehicle.SetupTradeDetails._

object SetupTradeDetailsPage extends Page with WebBrowserDSL {
  final val address = "/disposal-of-vehicle/setup-trade-details"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  final override val title: String = "Provide your Trader details" 

  def traderName(implicit driver: WebDriver): TextField = textField(id(TraderNameId))

  def traderPostcode(implicit driver: WebDriver): TextField = textField(id(TraderPostcodeId))

  def lookup(implicit driver: WebDriver): Element = find(id(SubmitId)).get

  def happyPath(traderBusinessName: String = TraderBusinessNameValid, traderBusinessPostcode: String = PostcodeValid)(implicit driver: WebDriver) = {
    go to SetupTradeDetailsPage
    traderName enter traderBusinessName
    traderPostcode enter traderBusinessPostcode
    click on lookup
  }

  def submitInvalidPostcode(implicit driver: WebDriver) = {
    go to SetupTradeDetailsPage
    traderName enter TraderBusinessNameValid
    traderPostcode enter PostcodeInvalid
    click on lookup
  }
}