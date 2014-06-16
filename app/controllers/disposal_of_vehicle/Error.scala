package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.Logger
import com.google.inject.Inject
import utils.helpers.CryptoHelper
import mappings.common.Languages._
import play.api.Play.current

final class Error @Inject()() extends Controller {
  def present(exceptionDigest: String) = Action { implicit request =>
    Logger.debug("Error - Displaying generic error page")
    Ok(views.html.disposal_of_vehicle.error(exceptionDigest))
  }

  def submit(exceptionDigest: String) = Action.async { implicit request =>
    Logger.debug("Error submit called - now removing full set of cookies and redirecting to Start page")
    CryptoHelper.discardAllCookies
  }

  def withLanguageCy(exceptionDigest: String) = Action { implicit request =>
    Redirect(routes.Error.present(exceptionDigest)).
      withLang(langCy)
  }

  def withLanguageEn(exceptionDigest: String) = Action { implicit request =>
    Redirect(routes.Error.present(exceptionDigest)).
      withLang(langEn)
  }
}
