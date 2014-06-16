package utils.helpers

import javax.crypto.BadPaddingException

import common.InvalidSessionException
import controllers.disposal_of_vehicle.routes
import play.api.Logger
import play.api.libs.Codecs
import play.api.mvc.Results._
import play.api.mvc.{SimpleResult, RequestHeader}

import scala.concurrent.{ExecutionContext, Future}

object ErrorStrategy {
  def apply(request: RequestHeader, ex: Throwable)(implicit executionContext: ExecutionContext): Future[SimpleResult] =
    ex.getCause match {
      case _: BadPaddingException  => CryptoHelper.handleApplicationSecretChange(request)
      case _: InvalidSessionException  => CryptoHelper.handleApplicationSecretChange(request)
      case cause =>
        val exceptionDigest = Codecs.sha1(Option(cause).fold("")(c => Option(c.getMessage).getOrElse("")))
        Logger.error(s"Exception thrown with digest '$exceptionDigest'", cause)
        Future(Redirect(routes.Error.present(exceptionDigest)))
    }
}
