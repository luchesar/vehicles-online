package common

import play.api.mvc.Cookie

trait CookieFlags {
  def applyToCookie(cookie: Cookie): Cookie
}

final class NoCookieFlags extends CookieFlags {
  override def applyToCookie(cookie: Cookie): Cookie = cookie
}

final class CookieFlagsFromConfig extends CookieFlags {
  import utils.helpers.Config.{secureCookies, cookieMaxAge}

  override def applyToCookie(cookie: Cookie): Cookie =
    cookie.copy(
      secure = secureCookies,
      maxAge = Some(cookieMaxAge))
}
