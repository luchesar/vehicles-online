package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.Logger

final class DuplicateDisposalError extends Controller {
  def present = Action { implicit request =>
    Logger.debug(s"Displaying duplicate disposal error page")
    Ok(views.html.disposal_of_vehicle.duplicate_disposal_error())
  }
}
