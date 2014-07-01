package pages.disposal_of_vehicle

import helpers.webbrowser.{Page, WebBrowserDSL, WebDriverFactory}

object SoapEndpointErrorPage extends Page with WebBrowserDSL {
  override val url = WebDriverFactory.testUrl + address.substring(1)
  final override val title = "We are sorry"
  final val address = "/disposal-of-vehicle/soap-endpoint-error"
}
