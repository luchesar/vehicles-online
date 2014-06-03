package common

import play.api.mvc.{Cookie, SimpleResult}
import com.google.inject.Inject
import java.util.UUID

class ClearTextClientSideSessionFactory @Inject()(implicit cookieFlags: CookieFlags) extends ClientSideSessionFactory {

  override protected def newSession(result: SimpleResult): (SimpleResult, ClientSideSession) = {
    val sessionSecretKeyPrefixCookie = Cookie(
      name = ClientSideSessionFactory.SessionIdCookieName,
      value = UUID.randomUUID().toString.take(20),
      secure = false,
      maxAge = None)
    val resultWithSessionSecretKeyCookie = result.withCookies(sessionSecretKeyPrefixCookie)

    (resultWithSessionSecretKeyCookie, new ClearTextClientSideSession(sessionSecretKeyPrefixCookie.value))
  }

  override def getSession(request: Traversable[Cookie]): Option[ClientSideSession] =
    getTrackingId(request).map(trackingId => new ClearTextClientSideSession(trackingId))
  //    Some(new ClearTextClientSideSession(getTrackingId(request).getOrElse("defaultTrackingId")))
}