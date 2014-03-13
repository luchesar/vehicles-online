package pages.disposal_of_vehicle

import helpers.webbrowser.{Element, TextField, WebBrowserDSL, Page}
import org.openqa.selenium.WebDriver
import helpers.disposal_of_vehicle.Helper._

object PostcodePage extends Page with WebBrowserDSL {
  override val url: String = ""
  override val title: String = ""

  def postcode(implicit driver: WebDriver): TextField = textField(id("postcodeId"))

  def submit(implicit driver: WebDriver): Element = find(xpath("//button[@type='submit' and @name=\"action\"]")).get

  def happyPath(implicit driver: WebDriver, postcode: String = postcodeValid) {
    go to PostcodePage
    PostcodePage.postcode enter postcode
    click on PostcodePage.submit
 }

  def sadPath(implicit driver: WebDriver, postcode: String) {
    go to PostcodePage
    PostcodePage.postcode enter postcode
    click on PostcodePage.submit
  }
}
