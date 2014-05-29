package common

import play.api.http.HeaderNames._
import play.api.mvc._

object CookieHelper {
  def fetchCookiesFromHeaders(result: SimpleResult) = result.header.headers.get(SET_COOKIE).toSeq.flatMap(Cookies.decode)
}
