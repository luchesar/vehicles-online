package controllers.disposal_of_vehicle

import play.api.mvc._

class UprnNotFound extends Controller {
  def present = Action { implicit request =>
    Ok(views.html.disposal_of_vehicle.uprn_not_found())
  }
}
