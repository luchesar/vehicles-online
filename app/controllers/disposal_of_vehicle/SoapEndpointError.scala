package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.Logger
import mappings.common.Languages._
import play.api.Play.current

final class SoapEndpointError extends Controller {
  def present = Action { implicit request =>
    Logger.debug(s"Displaying the micro-service error page")
    Ok(views.html.disposal_of_vehicle.soap_endpoint_error())
  }

  def withLanguageCy = Action { implicit request =>
    Redirect(routes.SoapEndpointError.present()).
      withLang(langCy)
  }

  def withLanguageEn = Action { implicit request =>
    Redirect(routes.SoapEndpointError.present()).
      withLang(langEn)
  }
}
