package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.Logger
import mappings.disposal_of_vehicle.RelatedCacheKeys
import common.EncryptedCookieImplicits
import EncryptedCookieImplicits.SimpleResultAdapter
import com.google.inject.Inject
import utils.helpers.{CryptoHelper, CookieNameHashing, CookieEncryption}

class Error @Inject()()(implicit encryption: CookieEncryption, cookieNameHashing: CookieNameHashing) extends Controller {
  def present = Action { implicit request =>
    Logger.debug("Error - displaying the generic error page")
    Ok(views.html.disposal_of_vehicle.error())
  }

  def submit = Action.async { implicit request =>
    Logger.debug("Error submit called - now going to remove full set of cookies and redirect to start page...")
    CryptoHelper.discardAllCookies
  }

}
