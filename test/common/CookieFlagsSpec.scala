package common

import org.scalatest.{Matchers, WordSpec}
import play.api.test.{FakeApplication, WithApplication}
import play.api.mvc.Cookie
import scala.concurrent.duration._
import scala.language.postfixOps

class CookieFlagsSpec extends WordSpec with Matchers {

  private final val TenMinutesInSeconds = (10 minutes).toSeconds.toInt

  "CookieFlagsFromConfig" should {
    "return a cookie with max age and secure properties set" in new WithApplication(app = fakeAppWithCookieConfig) {
      val cookieFlags = new CookieFlagsFromConfig
      val originalCookie = Cookie(name = "testCookieName", value = "testCookieValue")

      originalCookie.secure should equal(false)
      originalCookie.maxAge should equal(None)

      val modifiedCookie = cookieFlags.applyToCookie(originalCookie)
      modifiedCookie.secure should equal(true)
      modifiedCookie.maxAge should equal(Some(TenMinutesInSeconds))
    }
  }

  private val fakeAppWithCookieConfig = FakeApplication(
    additionalConfiguration = Map("secureCookies" -> true, "cookieMaxAge" -> TenMinutesInSeconds))
}