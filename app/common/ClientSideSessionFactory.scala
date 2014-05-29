package common

import play.api.mvc.{SimpleResult, Request}

trait ClientSideSessionFactory {
  def newSession(request: Request[_], result: SimpleResult): (SimpleResult, ClientSideSession)
  def getSession(request: Request[_]): Option[ClientSideSession]

  def ensureSession(request: Request[_], result: SimpleResult): (SimpleResult, ClientSideSession) =
    getSession(request)
      .map((result, _))
      .getOrElse(newSession(request, result))
}

