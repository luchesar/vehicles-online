package common

import play.api.mvc.{Cookie, SimpleResult}

trait ClientSideSessionFactory {
  def newSession(result: SimpleResult): (SimpleResult, ClientSideSession)
  def getSession(request: Traversable[Cookie]): Option[ClientSideSession]

  def ensureSession(request: Traversable[Cookie], result: SimpleResult): (SimpleResult, ClientSideSession) =
    getSession(request)
      .map((result, _))
      .getOrElse(newSession(result))
}

