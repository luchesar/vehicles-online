package common

import play.api.mvc.Cookie

trait ClientSideSession {
  val trackingId: String
  def nameCookie(key: String): CookieName
  def newCookie(name: CookieName, value: String): Cookie
  def getCookieValue(cookie: Cookie): String
}