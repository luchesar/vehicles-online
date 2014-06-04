package common

import app.ConfigProperties._
import play.api.mvc.Cookie
import scala.Some
import scala.concurrent.duration._
import scala.language.postfixOps
import com.google.inject.Inject

trait CookieFlags {
  def applyToCookie(cookie: Cookie): Cookie
}

final class NoCookieFlags extends CookieFlags {
  override def applyToCookie(cookie: Cookie): Cookie = cookie
}

final class CookieFlagsFromConfig @Inject()() extends CookieFlags {

  val cookieMaxAge: Int = getProperty("cookieMaxAge", (30 minutes).toSeconds.toInt)
  val secureCookies: Boolean = getProperty("secureCookies", default = true)

  override def applyToCookie(cookie: Cookie): Cookie = {
    cookie.copy(
      secure = secureCookies,
      maxAge = Some(cookieMaxAge))
  }
}
