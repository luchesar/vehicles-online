package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import services.fakes.FakeDateServiceImpl._
import mappings.disposal_of_vehicle.Dispose._
import mappings.common.DayMonthYear._
import services.fakes.FakeDisposeWebServiceImpl._

object DisposePage extends Page with WebBrowserDSL {
  final val address = "/disposal-of-vehicle/dispose"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  final override val title: String = "Complete & confirm"

  def mileage(implicit driver: WebDriver): TextField = textField(id(MileageId))

  def dateOfDisposalDay(implicit driver: WebDriver): SingleSel = singleSel(id(s"${DateOfDisposalId}_$DayId"))

  def dateOfDisposalMonth(implicit driver: WebDriver): SingleSel = singleSel(id(s"${DateOfDisposalId}_$MonthId"))

  def dateOfDisposalYear(implicit driver: WebDriver): SingleSel = singleSel(id(s"${DateOfDisposalId}_$YearId"))

  def consent(implicit driver: WebDriver): Checkbox = checkbox(id(ConsentId))

  def lossOfRegistrationConsent(implicit driver: WebDriver): Checkbox = checkbox(id(LossOfRegistrationConsentId))

  def back(implicit driver: WebDriver): Element = find(id(BackId)).get

  def dispose(implicit driver: WebDriver): Element = find(id(SubmitId)).get

  def happyPath(implicit driver: WebDriver) = {
    go to DisposePage
    mileage enter MileageValid
    dateOfDisposalDay select DateOfDisposalDayValid
    dateOfDisposalMonth select DateOfDisposalMonthValid
    dateOfDisposalYear select DateOfDisposalYearValid
    click on consent
    click on lossOfRegistrationConsent
    click on dispose
  }

  def sadPath(implicit driver: WebDriver) = {
    go to DisposePage
    dateOfDisposalDay select ""
    dateOfDisposalMonth select ""
    dateOfDisposalYear select ""
    click on dispose
  }
}