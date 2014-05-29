package utils.helpers

import play.api.mvc.{DiscardingCookie, RequestHeader, SimpleResult}
import play.api.Logger
import mappings.disposal_of_vehicle.RelatedCacheKeys
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.Results._
import controllers.disposal_of_vehicle.routes
import ExecutionContext.Implicits.global

object CryptoHelper {

  def handleApplicationSecretChange(implicit request: RequestHeader): Future[SimpleResult] = discardAllCookies

  def discardAllCookies(implicit request: RequestHeader): Future[SimpleResult] = {
    Logger.warn("Handling BadPaddingException or IllegalBlockSizeException by removing all cookies except seen cookie. " +
      "Has the application secret changed or has a user tampered with his session secret ?")

    Future {
      val discardingCookiesKeys = request.cookies.map(_.name).filter(_ != RelatedCacheKeys.SeenCookieMessageKey)
      val discardingCookies = discardingCookiesKeys.map(DiscardingCookie(_)).toSeq
      Redirect(routes.BeforeYouStart.present())
        .discardingCookies(discardingCookies: _*)
    }
  }
}
