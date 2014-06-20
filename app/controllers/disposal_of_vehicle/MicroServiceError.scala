package controllers.disposal_of_vehicle

import play.api.mvc._

final class MicroServiceError extends Controller {
  def present = Action { implicit request =>
    Ok(views.html.disposal_of_vehicle.micro_service_error())
  }
}
