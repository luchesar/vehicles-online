package pages.disposal_of_vehicle

import helpers.webbrowser.{WebBrowserDSL, Page, WebDriverFactory}

object VrmLockedPage extends Page with WebBrowserDSL {
  final val address = "/disposal-of-vehicle/vrm-locked"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)

  final override val title = "Registration mark is locked"
}
