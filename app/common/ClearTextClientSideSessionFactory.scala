package common

import play.api.mvc.{Cookie, SimpleResult}
import com.google.inject.Inject
import java.util.UUID


object ClearTextClientSideSessionFactory {
  final val DefaultTrackingId = "default-test-tracking-id"
}

class ClearTextClientSideSessionFactory @Inject()(implicit cookieFlags: CookieFlags) extends ClientSideSessionFactory {

  override def newSessionCookiesIfNeeded(request: Traversable[Cookie]): Option[Seq[Cookie]] = None

  override def getSession(request: Traversable[Cookie]): ClientSideSession =
    getTrackingId(request) match {
      case Some(trackingId) => new ClearTextClientSideSession(trackingId)
      case None => new ClearTextClientSideSession(ClearTextClientSideSessionFactory.DefaultTrackingId)
    }

  private def getTrackingId(request: Traversable[Cookie]): Option[String] = {
    request.find(_.name == ClientSideSessionFactory.SessionIdCookieName).map {
      _.value
    }
  }
}