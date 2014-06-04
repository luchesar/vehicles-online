package common

import play.api.mvc.{Cookie, SimpleResult}
import com.google.inject.Inject
import java.util.UUID


object ClearTextClientSideSessionFactory {
  final val DefaultTrackingId = "default-test-tracking-id"
}

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
    getTrackingId(request).
      map(trackingId => new ClearTextClientSideSession(trackingId)).
      orElse(Some(new ClearTextClientSideSession(ClearTextClientSideSessionFactory.DefaultTrackingId)))
}