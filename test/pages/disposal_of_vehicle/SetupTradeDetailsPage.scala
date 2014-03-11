package pages.disposal_of_vehicle

import org.openqa.selenium.{By, WebDriver}
import helpers.webbrowser._
import helpers.disposal_of_vehicle.Helper._
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import play.api.test.TestBrowser
import mappings.disposal_of_vehicle.SetupTradeDetails._
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import java.util.concurrent.TimeUnit
import scala.util.Try

object SetupTradeDetailsPage extends Page with WebBrowserDSL {

  override val url: String = WebDriverFactory.testUrl + "disposal-of-vehicle/setup-trade-details"
  override val title: String = "Dispose a vehicle into the motor trade: set-up"

  def dealerName(implicit driver: WebDriver): TextField = textField(id("dealerName"))

  def dealerPostcode(implicit driver: WebDriver): TextField = textField(id("dealerPostcode"))

  def lookup(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get


  def happyPath(implicit driver: WebDriver, traderBusinessName: String = traderBusinessNameValid, traderPostcode: String = traderPostcodeValid) = {
    go to SetupTradeDetailsPage
    SetupTradeDetailsPage.dealerName enter traderBusinessName
    SetupTradeDetailsPage.dealerPostcode enter traderPostcode
    click on SetupTradeDetailsPage.lookup
  }

}