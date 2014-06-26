package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.ClientSideSessionFactory
import common.CookieImplicits.RichSimpleResult
import mappings.disposal_of_vehicle.RelatedCacheKeys
import play.api.mvc._
import utils.helpers.Config

final class BeforeYouStart @Inject()(implicit clientSideSessionFactory: ClientSideSessionFactory, config: Config) extends Controller {

  def present = Action { implicit request =>
    Ok(views.html.disposal_of_vehicle.before_you_start(config.prototypeBannerVisible)).
      withNewSession.
      discardingCookies(RelatedCacheKeys.FullSet)
  }

  def submit = Action { implicit request =>
    Redirect(routes.SetUpTradeDetails.present())
  }

}