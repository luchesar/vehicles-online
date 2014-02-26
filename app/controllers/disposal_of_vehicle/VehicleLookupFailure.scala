package controllers.disposal_of_vehicle

import play.api.Logger
import play.api.mvc._

object VehicleLookupFailure extends Controller {
  def present = Action {
    implicit request =>
      val uniqueId = java.util.UUID.randomUUID.toString
      Logger.debug(s"VehicleLookupFailure - storing the following in session: modelId = $uniqueId")
      Ok(views.html.disposal_of_vehicle.vehicle_lookup_failure()).withSession("modelId" -> uniqueId)
  }

  def submit = Action { implicit request =>
    val modelId = request.session.get("modelId")
    Logger.debug(s"VehicleLookupFailure - reading modelId from session: $modelId")
    Redirect(routes.VehicleLookup.present)
  }
}
