package controllers.disposal_of_vehicle

import play.api.Logger
import play.api.mvc._
import controllers.disposal_of_vehicle.Helpers._
import scala.Some

object VehicleLookupFailure extends Controller {
  def present = Action {
    implicit request =>
      (fetchDealerDetailsFromCache, fetchVehicleDetailsFromCache) match {
        case (Some(dealerDetails), Some(vehicleDetails)) => {
          Logger.debug("found dealer details")
          Ok(views.html.disposal_of_vehicle.vehicle_lookup_failure(vehicleDetails))
        }
        case _ => Redirect(routes.SetUpTradeDetails.present)
      }
  }

  def submit = Action { implicit request =>
    val modelId = request.session.get("modelId")
    Logger.debug(s"VehicleLookupFailure - reading modelId from session: $modelId")
    Redirect(routes.VehicleLookup.present)
  }
}


