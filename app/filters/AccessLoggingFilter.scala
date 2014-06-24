package filters

import com.google.inject.Inject
import play.api.mvc.{SimpleResult, RequestHeader, Filter}
import scala.concurrent.Future
import java.text.SimpleDateFormat
import java.util.Date
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.http.HeaderNames._
import common.ClientSideSessionFactory
import services.HttpHeaders._
import play.api.Logger

class AccessLoggingFilter @Inject()(logger: ClfLogger) extends Filter {
  val accessLogger = Logger("dvla.common.AccessLogger")

  override def apply(filter: (RequestHeader) => Future[SimpleResult])(requestHeader: RequestHeader): Future[SimpleResult] = {
    val requestTimestamp = new Date()
    filter(requestHeader).map {result =>
      val accessLogMessage = logger.clfEntry(requestTimestamp, requestHeader, result)
      accessLogger.info(accessLogMessage)
      result
    }
  }

}

class ClfLogger {

  def clfEntry(requestTimestamp: Date, request: RequestHeader, result: SimpleResult) : String = {
    val ipAddress = Seq(
      request.headers.get(XForwardedFor),
      Option(request.remoteAddress),
      request.headers.get(XRealIp),
      Some("-")
    ).flatten.head

    val trackingId = request.cookies.get(ClientSideSessionFactory.TrackingIdCookieName) match {
     case Some(c) => c.value
     case _ => "-"
    }

    val method = request.method
    val uri = request.uri
    val protocol = request.version
    val date = s"[${ClfLogger.dateFormat.format(requestTimestamp)}]"
    val responseCode = result.header.status
    val responseLength = result.header.headers.get(CONTENT_LENGTH).getOrElse("-")

    s"""$ipAddress - - $date "$method $uri $protocol" $responseCode $responseLength "$trackingId" """
  }

}

object ClfLogger {
  val dateFormat = new SimpleDateFormat("dd/MMM/yyyy:hh:mm:ss +SSS")
}


