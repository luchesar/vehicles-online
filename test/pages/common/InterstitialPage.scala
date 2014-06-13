package pages.common

import helpers.webbrowser._

object InterstitialPage extends Page with WebBrowserDSL {
  final val address = "/common/interstitial"
  override val url: String = WebDriverFactory.testUrl + address.substring(1)
  final override val title: String = "Redirecting"
}