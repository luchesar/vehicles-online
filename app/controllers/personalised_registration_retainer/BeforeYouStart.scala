package controllers.personalised_registration_retainer

import play.api.mvc._
import com.google.inject.Inject
import common.{ClientSideSessionFactory, CookieImplicits}
import CookieImplicits.SimpleResultAdapter
import mappings.disposal_of_vehicle.RelatedCacheKeys

final class BeforeYouStart @Inject()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  def present = Action { implicit request =>

    Ok(views.html.personalised_registration_retainer.before_you_start()).
      withNewSession.
      discardingCookies(RelatedCacheKeys.FullSet)
  }

}
