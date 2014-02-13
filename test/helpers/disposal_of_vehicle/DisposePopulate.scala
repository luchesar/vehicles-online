package helpers.disposal_of_vehicle

import play.api.test.TestBrowser
import mappings.disposal_of_vehicle.Dispose._
import models.DayMonthYear
import play.api.Play.current

object DisposePopulate {
  val url = "/disposal-of-vehicle/dispose"

  def happyPath(browser: TestBrowser) = {
    browser.goTo(url)

    // Do not click the consent checkbow as it already pre-populated
    //      browser.click(s"#${consentId}")

    browser.click(s"#${dateOfDisposalId}_day option[value='1']")
    browser.click(s"#${dateOfDisposalId}_month option[value='1']")
    browser.fill(s"#${dateOfDisposalId}_year") `with` "2000"

    browser.submit("button[type='submit']")
  }

  def setupCache() = {
    val key = mappings.disposal_of_vehicle.Dispose.cacheKey
    val value = DayMonthYear.today

    play.api.cache.Cache.set(key, value)
  }
}
