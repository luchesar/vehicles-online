package pages.disposal_of_vehicle

import helpers.webbrowser.{Element, Page, WebBrowserDSL, WebDriverFactory}
import mappings.disposal_of_vehicle.BeforeYouStart.NextId
import org.openqa.selenium.WebDriver

object BeforeYouStartPage extends Page with WebBrowserDSL {
  override val url = WebDriverFactory.testUrl
  final override val title = "Sell a vehicle into the motor trade"
  final val address = "/disposal-of-vehicle/before-you-start"
  final val progressStep = "Step 1 of 6"
  final val titleCy = "Cael gwared cerbyd i mewn i'r fasnach foduron"


  def startNow(implicit driver: WebDriver): Element = find(id(NextId)).get
}