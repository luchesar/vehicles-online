package common

import app.ConfigProperties._
import com.google.inject.Inject
import play.api.mvc.Cookie

import scala.concurrent.duration._
import scala.language.postfixOps

trait CookieFlags {
  def applyToCookie(cookie: Cookie): Cookie
}

final class NoCookieFlags extends CookieFlags {
  override def applyToCookie(cookie: Cookie): Cookie = cookie
}

final class CookieFlagsFromConfig @Inject()() extends CookieFlags {

  private val cookieMaxAgeSeconds = getProperty("cookieMaxAge", (30 minutes).toSeconds.toInt)
  private val secureCookies = getProperty("secureCookies", default = true)

  override def applyToCookie(cookie: Cookie): Cookie =
    cookie.copy(
      secure = secureCookies,
      maxAge = Some(cookieMaxAgeSeconds))
}
