package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.Logger
import utils.helpers.{CookieNameHashing, CookieEncryption}
import com.google.inject.Inject
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState.SimpleResultAdapter
import mappings.disposal_of_vehicle.RelatedCacheKeys

class BeforeYouStart @Inject()(implicit encryption: CookieEncryption, cookieNameHashing: CookieNameHashing) extends Controller {

  def present = Action { implicit request =>

    Ok(views.html.disposal_of_vehicle.before_you_start()).
      withNewSession.
      discardingEncryptedCookies(RelatedCacheKeys.FullSet)
  }

  def submit = Action { implicit request =>
    val modelId = request.session.get("modelId")
    Logger.debug(s"BeforeYouStart - reading modelId from session: $modelId")
    Redirect(routes.SetUpTradeDetails.present())
  }
}
