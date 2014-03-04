package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.Config
import helpers.WebBrowser

// TODO Export this class as top-level class. This 'trait' is required as a result of a bug in ScalaTest.
// See https://github.com/scalatest/scalatest/issues/174
trait DisposePage extends WebBrowser { this :WebBrowser =>

   object DisposePage extends DisposePage

   class DisposePage extends Page {

     override val url: String = Config.baseUrl + "disposal-of-vehicle/dispose"
     override val title: String = ???

     def mileage(implicit driver: WebDriver): TextField = textField(id("mileage"))

     def dateOfDisposalDay(implicit driver: WebDriver): SingleSel = singleSel(id("dateOfDisposal_day"))

     def dateOfDisposalMonth(implicit driver: WebDriver): SingleSel = singleSel(id("dateOfDisposal_month"))

     def dateOfDisposalYear(implicit driver: WebDriver): TelField = telField(id("dateOfDisposal_year"))

     def consent(implicit driver: WebDriver): Checkbox = checkbox(id("consent"))

     def emailAddress(implicit driver: WebDriver): TextField = textField(id("emailAddress"))

     def back(implicit driver: WebDriver): Element = find(id("backButton")).get

     def dispose(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

   }
 }