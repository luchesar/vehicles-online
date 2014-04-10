package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import services.fakes.FakeDateServiceImpl._
import mappings.disposal_of_vehicle.Dispose._
import mappings.common.DayMonthYear._

object DisposePage extends Page with WebBrowserDSL {
  val address = "/disposal-of-vehicle/dispose"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  override val title: String = "Complete & confirm"

  def mileage(implicit driver: WebDriver): TextField = textField(id(mileageId))

  def dateOfDisposalDay(implicit driver: WebDriver): SingleSel = singleSel(id(s"${dateOfDisposalId}_$dayId"))

  def dateOfDisposalMonth(implicit driver: WebDriver): SingleSel = singleSel(id(s"${dateOfDisposalId}_$monthId"))

  def dateOfDisposalYear(implicit driver: WebDriver): SingleSel = singleSel(id(s"${dateOfDisposalId}_$yearId"))

  def consent(implicit driver: WebDriver): Checkbox = checkbox(id(consentId))

  def lossOfRegistrationConsent(implicit driver: WebDriver): Checkbox = checkbox(id(lossOfRegistrationConsentId))

  def back(implicit driver: WebDriver): Element = find(id("backButton")).get

  def dispose(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

  def happyPath(implicit driver: WebDriver) = {
    go to DisposePage.url
    DisposePage.mileage enter "50000" // TODO don't use magic number, use a constant!
    DisposePage.dateOfDisposalDay select dateOfDisposalDayValid
    DisposePage.dateOfDisposalMonth select dateOfDisposalMonthValid
    DisposePage.dateOfDisposalYear select dateOfDisposalYearValid
    click on DisposePage.consent
    click on DisposePage.lossOfRegistrationConsent
    click on DisposePage.dispose
  }

  def sadPath(implicit driver: WebDriver) = {
    go to DisposePage.url
    DisposePage.dateOfDisposalDay select ""
    DisposePage.dateOfDisposalMonth select ""
    DisposePage.dateOfDisposalYear select ""
    click on DisposePage.dispose
  }
}