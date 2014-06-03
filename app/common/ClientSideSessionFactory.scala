package common

import play.api.mvc.{Cookies, Cookie, SimpleResult}
import play.api.http.HeaderNames

object ClientSideSessionFactory {
  final val SessionIdCookieName = "tracking-id"
}

trait ClientSideSessionFactory {
  protected def newSession(result: SimpleResult): (SimpleResult, ClientSideSession)

  def getSession(request: Traversable[Cookie]): Option[ClientSideSession]

  protected def getTrackingId(request: Traversable[Cookie]): Option[String] = {
    request.find(_.name == ClientSideSessionFactory.SessionIdCookieName).map { _  .value}
  }

  def ensureSession(request: Traversable[Cookie], result: SimpleResult): (SimpleResult, ClientSideSession) = {
    val allCookies = result.header.headers.get(HeaderNames.SET_COOKIE).fold(request)(Cookies.decode)
    getSession(allCookies).fold(newSession(result))((result, _))
  }
}

