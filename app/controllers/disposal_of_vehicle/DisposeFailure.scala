package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.Logger

object DisposeFailure extends Controller {

  def present = Action {
    implicit request =>
      val uniqueId = java.util.UUID.randomUUID.toString
      Logger.debug(s"DisposeFailure - storing the following in session: modelId = $uniqueId")
      Ok(views.html.disposal_of_vehicle.dispose_failure()).withSession("modelId" -> uniqueId)
  }

  def submit = Action { implicit request =>
    val modelId = request.session.get("modelId")
    Logger.debug(s"DisposeFailure - reading modelId from session: $modelId")
    Redirect(routes.VehicleLookup.present)
  }
}
