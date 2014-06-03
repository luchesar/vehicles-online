package common

import org.scalatest.{Matchers, WordSpec}
import play.api.test.{FakeApplication, WithApplication}
import play.api.mvc.Cookie
import scala.concurrent.duration._
import scala.language.postfixOps
import helpers.webbrowser.TestGlobal

final class CookieFlagsSpec extends WordSpec with Matchers {
  "CookieFlagsFromConfig" should {
    "return a cookie with max age and secure properties set" in new WithApplication(app = fakeAppWithCookieConfig) {
      val cookieFlags = new CookieFlagsFromConfig
      val originalCookie = Cookie(name = "testCookieName", value = "testCookieValue")

      originalCookie.secure should equal(false)
      originalCookie.maxAge should equal(None)

      val modifiedCookie = cookieFlags.applyToCookie(originalCookie) // This will load values from the fake config we are passing into this test's WithApplication.
      modifiedCookie.secure should equal(true)
      modifiedCookie.maxAge should equal(Some(TenMinutesInSeconds))
    }
  }

  private final val TenMinutesInSeconds = (10 minutes).toSeconds.toInt

  private val fakeAppWithCookieConfig = FakeApplication(
    withGlobal = Some(TestGlobal),
    additionalConfiguration = Map("secureCookies" -> true, "cookieMaxAge" -> TenMinutesInSeconds))
}