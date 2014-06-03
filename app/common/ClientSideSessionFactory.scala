package common

import play.api.mvc.{Cookie, SimpleResult}

object ClientSideSessionFactory {
  final val SessionIdCookieName = "tracking-id"
}

trait ClientSideSessionFactory {
  protected def newSession(result: SimpleResult): (SimpleResult, ClientSideSession)

  def getSession(request: Traversable[Cookie]): Option[ClientSideSession]

  protected def getTrackingId(request: Traversable[Cookie]): Option[String] = {
    request.find(_.name == ClientSideSessionFactory.SessionIdCookieName).map { cookie => cookie.value}
  }

  def ensureSession(request: Traversable[Cookie], result: SimpleResult): (SimpleResult, ClientSideSession) =
    getSession(request)
      .map((result, _))
      .getOrElse(newSession(result))
}

