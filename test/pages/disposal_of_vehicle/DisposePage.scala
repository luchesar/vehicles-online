package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.WebDriverFactory
import helpers.webbrowser._

object DisposePage extends Page with WebBrowserDSL {

  override val url: String = WebDriverFactory.baseUrl + "disposal-of-vehicle/dispose"
  override val title: String = "Dispose a vehicle into the motor trade: confirm"

  def mileage(implicit driver: WebDriver): TextField = textField(id("mileage"))

  def dateOfDisposalDay(implicit driver: WebDriver): SingleSel = singleSel(id("dateOfDisposal_day"))

  def dateOfDisposalMonth(implicit driver: WebDriver): SingleSel = singleSel(id("dateOfDisposal_month"))

  def dateOfDisposalYear(implicit driver: WebDriver): TelField = telField(id("dateOfDisposal_year"))

  def emailAddress(implicit driver: WebDriver): TextField = textField(id("emailAddress"))

  def back(implicit driver: WebDriver): Element = find(id("backButton")).get

  def dispose(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get
}