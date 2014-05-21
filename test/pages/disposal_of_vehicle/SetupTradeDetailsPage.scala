package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import helpers.disposal_of_vehicle.Helper._
import services.fakes.FakeAddressLookupService._
import mappings.disposal_of_vehicle.SetupTradeDetails._

object SetupTradeDetailsPage extends Page with WebBrowserDSL {
  final val address = "/disposal-of-vehicle/setup-trade-details"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  final override val title: String = "Provide your trader details"

  def traderName(implicit driver: WebDriver): TextField = textField(id(TraderNameId))

  def traderPostcode(implicit driver: WebDriver): TextField = textField(id(TraderPostcodeId))

  def lookup(implicit driver: WebDriver): Element = find(id(SubmitId)).get

  def happyPath(traderBusinessName: String = traderBusinessNameValid, traderBusinessPostcode: String = postcodeValid)(implicit driver: WebDriver) = {
    go to SetupTradeDetailsPage
    traderName enter traderBusinessName
    traderPostcode enter traderBusinessPostcode
    click on lookup
  }

  def submitInvalidPostcode(implicit driver: WebDriver) = {
    go to SetupTradeDetailsPage
    traderName enter traderBusinessNameValid
    traderPostcode enter postcodeInvalid
    click on lookup
  }
}