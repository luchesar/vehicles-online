package controllers.common

import com.google.inject.Inject
import common.ClientSideSessionFactory
import common.CookieImplicits.{RichCookies, RichSimpleResult}
import play.api.mvc._
import utils.helpers.Config
import mappings.common.Help.HelpCacheKey
import controllers.disposal_of_vehicle.routes.BeforeYouStart

final class Help @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory, config: Config) extends Controller {
  def present = Action { implicit request =>
    val origin = request.headers.get("Referer").getOrElse("No Referer in header")
    Ok(views.html.common.help()).
      withCookie(HelpCacheKey, origin) // Save the previous page URL (from the referer header) into a cookie.
  }

  def back = Action { implicit request =>
    val origin: String = request.cookies.getString(HelpCacheKey).getOrElse(BeforeYouStart.present().url)
    Redirect(origin)
  }
}
