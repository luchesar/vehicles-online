package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.Logger
import mappings.common.Languages._
import play.api.Play.current

final class MicroServiceError extends Controller {
  def present = Action { implicit request =>
    Logger.debug(s"Displaying the micro-service error page")
    Ok(views.html.disposal_of_vehicle.micro_service_error())
  }

  def withLanguageCy = Action { implicit request =>
    Redirect(routes.MicroServiceError.present()).
      withLang(langCy)
  }

  def withLanguageEn = Action { implicit request =>
    Redirect(routes.MicroServiceError.present()).
      withLang(langEn)
  }
}
