package pages.disposal_of_vehicle

import org.openqa.selenium.WebDriver
import helpers.webbrowser._
import mappings.disposal_of_vehicle.BeforeYouStart._
import mappings.common.Languages._

object BeforeYouStartPage extends Page with WebBrowserDSL {
  final val address = "/disposal-of-vehicle/before-you-start"
  override val url: String = WebDriverFactory.testUrl
  final override val title: String = "Sell a vehicle into the motor trade"

  def startNow(implicit driver: WebDriver): Element = find(id(NextId)).get
  def cymraeg(implicit driver: WebDriver): Element = find(id(CyId)).get
  def english(implicit driver: WebDriver): Element = find(id(EnId)).get
  def hasCymraeg(implicit driver: WebDriver): Boolean = find(id(CyId)).isDefined
  def hasEnglish(implicit driver: WebDriver): Boolean = find(id(EnId)).isDefined
}