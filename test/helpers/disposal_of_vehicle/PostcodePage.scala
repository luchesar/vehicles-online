package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import mappings.common.Postcode
import Postcode.postcodeId

object PostcodePage {
  val url = "/vehicles/postcode"
  val title = "Vehicles: Postcode"
  val postcodeValid = "SE1 6EH"

  def happyPath(browser: TestBrowser) {
    browser.goTo(url)

    browser.fill(s"#${postcodeId}") `with` postcodeValid

    browser.submit("button[type='submit']")
  }

  def sadPath(browser: TestBrowser, postcode: String) {
    browser.goTo(url)

    browser.fill(s"#${postcodeId}") `with` postcode

    browser.submit("button[type='submit']")
  }
}
