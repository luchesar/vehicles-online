package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._

object DisposePage extends Page with WebBrowserDSL {
  val address = "disposal-of-vehicle/dispose"
  val urlControllerTest: String = "/" + address
  override val url: String = WebDriverFactory.testUrl + address
  override val title: String = "Dispose a vehicle into the motor trade: confirm"

  def mileage(implicit driver: WebDriver): TextField = textField(id("mileage"))

  def dateOfDisposalDay(implicit driver: WebDriver): SingleSel = singleSel(id("dateOfDisposal_day"))

  def dateOfDisposalMonth(implicit driver: WebDriver): SingleSel = singleSel(id("dateOfDisposal_month"))

  def dateOfDisposalYear(implicit driver: WebDriver): TelField = telField(id("dateOfDisposal_year"))

  def emailAddress(implicit driver: WebDriver): TextField = textField(id("emailAddress"))

  def back(implicit driver: WebDriver): Element = find(id("backButton")).get

  def dispose(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

  def happyPath(implicit driver: WebDriver) = {
    go to DisposePage.url
    DisposePage.mileage enter "50000"
    DisposePage.dateOfDisposalDay select "31"
    DisposePage.dateOfDisposalMonth select "12"
    DisposePage.dateOfDisposalYear enter "2013"
    DisposePage.emailAddress enter  "test@testemail.com"
    click on DisposePage.dispose
  }

  def sadPath(implicit driver: WebDriver) = {
    go to DisposePage.url
    DisposePage.dateOfDisposalDay select ""
    DisposePage.dateOfDisposalMonth select ""
    DisposePage.dateOfDisposalYear enter ""
    click on DisposePage.dispose
  }
}