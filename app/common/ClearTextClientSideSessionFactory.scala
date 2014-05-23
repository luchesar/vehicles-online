package common

import play.api.mvc.{SimpleResult, Request}
import com.google.inject.Inject

class ClearTextClientSideSessionFactory @Inject()(implicit cookieFlags: CookieFlags) extends ClientSideSessionFactory {

  // No intrinsic state so safe to return same instance for all sessions.
  private val clearTextClientSideSession = new ClearTextClientSideSession()

  override def newSession(request: Request[_], result: SimpleResult): (SimpleResult, ClientSideSession) =
    (result, clearTextClientSideSession)

  override def getSession(request: Request[_]): Option[ClientSideSession] =
    Some(clearTextClientSideSession)
}
