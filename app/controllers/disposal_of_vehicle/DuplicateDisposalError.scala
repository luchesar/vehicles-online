package controllers.disposal_of_vehicle

import play.api.mvc._

final class DuplicateDisposalError extends Controller {
  def present = Action { implicit request =>
    Ok(views.html.disposal_of_vehicle.duplicate_disposal_error())
  }
}
