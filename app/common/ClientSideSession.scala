package common

import play.api.mvc.Cookie

trait ClientSideSession {
  def nameCookie(key: String): CookieName
  def newCookie(name: CookieName, value: String): Cookie
  def getCookieValue(cookie: Cookie): String
  val trackingId: String
}