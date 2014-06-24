package controllers.disposal_of_vehicle

import play.api.mvc._

final class SoapEndpointError extends Controller {
  def present = Action { implicit request =>
    Ok(views.html.disposal_of_vehicle.soap_endpoint_error())
  }
}
