package utils.helpers

import java.util.Date
import javax.crypto.BadPaddingException

import com.google.inject.Inject
import com.google.inject.name.Named
import common.InvalidSessionException
import controllers.disposal_of_vehicle.routes
import filters.AccessLoggingFilter.AccessLoggerName
import filters.ClfEntryBuilder
import play.api.libs.Codecs
import play.api.mvc.Results.Redirect
import play.api.mvc.{RequestHeader, SimpleResult}
import play.api.{Logger, LoggerLike}

import scala.concurrent.{ExecutionContext, Future}

class ErrorStrategy @Inject()(clfEntryBuilder: ClfEntryBuilder,
                              @Named(AccessLoggerName) accessLogger: LoggerLike) {

  def apply(request: RequestHeader, ex: Throwable)
           (implicit executionContext: ExecutionContext): Future[SimpleResult] = {
    val result = ex.getCause match {
      case _: BadPaddingException => CryptoHelper.handleApplicationSecretChange(request)
      case _: InvalidSessionException => CryptoHelper.handleApplicationSecretChange(request)
      case cause =>
        val exceptionDigest = Codecs.sha1(Option(cause).fold("")(c => Option(c.getMessage).getOrElse("")))
        Logger.error(s"Exception thrown with digest '$exceptionDigest'", cause)
        Future(Redirect(routes.Error.present(exceptionDigest)))
    }
    result.map { result =>
      accessLogger.info(clfEntryBuilder.clfEntry(new Date(), request, result))
    }
    result
  }
}