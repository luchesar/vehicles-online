package common

import play.api.mvc.Cookie

class ClearTextClientSideSession()(implicit cookieFlags: CookieFlags) extends ClientSideSession {
  override def nameCookie(key: String): CookieName =
    CookieName(key)

  override def newCookie(name: CookieName, value: String): Cookie =
    cookieFlags.applyToCookie(Cookie(name.value, value))

  override def getCookieValue(cookie: Cookie): String =
    cookie.value
}
