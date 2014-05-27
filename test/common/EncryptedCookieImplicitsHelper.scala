package common

import play.api.http.HeaderNames._
import play.api.mvc._

object EncryptedCookieImplicitsHelper {
  implicit class SimpleResultAdapter(val inner: SimpleResult) extends AnyVal {
    def fetchCookiesFromHeaders = inner.header.headers.get(SET_COOKIE).toSeq.flatMap(Cookies.decode)
  }
}
