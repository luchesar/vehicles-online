package controllers.disposal_of_vehicle

import play.api.mvc._
import com.google.inject.Inject
import common.{ClientSideSessionFactory, CookieImplicits}
import CookieImplicits.SimpleResultAdapter
import mappings.disposal_of_vehicle.RelatedCacheKeys

final class BeforeYouStart @Inject()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  def present = Action { implicit request =>
    Ok(views.html.disposal_of_vehicle.before_you_start()).
      withNewSession.
      discardingCookies(RelatedCacheKeys.FullSet)
  }

  def submit = Action { implicit request =>
    Redirect(routes.SetUpTradeDetails.present())
  }

  def withLanguageCy = Action { implicit request =>
    Redirect(routes.BeforeYouStart.present()).
      withLang("cy")
  }

  def withLanguageEn = Action { implicit request =>
    Redirect(routes.BeforeYouStart.present()).
      withLang("en")
  }
}
