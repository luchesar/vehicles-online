package common

import play.api.mvc.{Cookie, SimpleResult}
import com.google.inject.Inject

class ClearTextClientSideSessionFactory @Inject()(implicit cookieFlags: CookieFlags) extends ClientSideSessionFactory {

  // No intrinsic state so safe to return same instance for all sessions.
  private val clearTextClientSideSession = new ClearTextClientSideSession()

  override def newSession(result: SimpleResult): (SimpleResult, ClientSideSession) =
    (result, clearTextClientSideSession)

  override def getSession(request: Traversable[Cookie]): Option[ClientSideSession] =
    Some(clearTextClientSideSession)
}
