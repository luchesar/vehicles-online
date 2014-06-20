package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.ClientSideSessionFactory
import common.CookieImplicits.RichCookies
import mappings.common.Interstitial.InterstitialCacheKey
import play.api.mvc._

final class Interstitial @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {
  def present = Action { implicit request =>
      val nextPageUrl = request.cookies.getString(InterstitialCacheKey) match {
        case Some(url) => url
        case None => routes.BeforeYouStart.present().url
      }
      Ok(views.html.common.interstitial(nextPageUrl))
  }
}
