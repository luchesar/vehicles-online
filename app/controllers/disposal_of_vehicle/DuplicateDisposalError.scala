package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.Logger
import mappings.common.Languages._
import play.api.Play.current

final class DuplicateDisposalError extends Controller {
  def present = Action { implicit request =>
    Logger.debug(s"Displaying duplicate disposal error page")
    Ok(views.html.disposal_of_vehicle.duplicate_disposal_error())
  }

  def withLanguageCy = Action { implicit request =>
    Redirect(routes.DuplicateDisposalError.present()).
      withLang(langCy)
  }

  def withLanguageEn = Action { implicit request =>
    Redirect(routes.DuplicateDisposalError.present()).
      withLang(langEn)
  }
}
