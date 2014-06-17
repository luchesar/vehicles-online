package controllers.disposal_of_vehicle

import play.api.mvc._
import mappings.common.AlternateLanguages._
import play.api.Play.current

final class UprnNotFound extends Controller {
  def present = Action { implicit request =>
    Ok(views.html.disposal_of_vehicle.uprn_not_found())
  }
}
