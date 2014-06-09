package common

import play.api.mvc.{Cookies, Cookie, SimpleResult}
import play.api.http.HeaderNames

object ClientSideSessionFactory {
  final val SessionIdCookieName = "tracking_id"
}

trait ClientSideSessionFactory {
  def getSession(request: Traversable[Cookie]): ClientSideSession

  def newSessionCookiesIfNeeded(request: Traversable[Cookie]): Option[Seq[Cookie]]
}

