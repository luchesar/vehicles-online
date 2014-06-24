package utils.helpers

import java.util.Date
import javax.crypto.BadPaddingException

import common.InvalidSessionException
import controllers.disposal_of_vehicle.routes
import filters.ClfLogger
import play.api.Logger
import play.api.libs.Codecs
import play.api.mvc.Results._
import play.api.mvc.{RequestHeader, SimpleResult}

import scala.concurrent.{ExecutionContext, Future}

object ErrorStrategy {
  private final val AccessLogger = Logger("dvla.common.AccessLogger")
  private final val ClfLogger = new ClfLogger()

  def apply(request: RequestHeader, ex: Throwable)(implicit executionContext: ExecutionContext): Future[SimpleResult] = {
    val result = ex.getCause match {
      case _: BadPaddingException => CryptoHelper.handleApplicationSecretChange(request)
      case _: InvalidSessionException => CryptoHelper.handleApplicationSecretChange(request)
      case cause =>
        val exceptionDigest = Codecs.sha1(Option(cause).fold("")(c => Option(c.getMessage).getOrElse("")))
        Logger.error(s"Exception thrown with digest '$exceptionDigest'", cause)
        Future(Redirect(routes.Error.present(exceptionDigest)))
    }
    result.map { result =>
      AccessLogger.info(ClfLogger.clfEntry(new Date(), request, result))
    }
    result
  }

}
