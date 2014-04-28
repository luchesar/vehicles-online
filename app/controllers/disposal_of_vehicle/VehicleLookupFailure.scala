package controllers.disposal_of_vehicle

import play.api.Logger
import play.api.mvc._
import scala.Some
import com.google.inject.Inject

class VehicleLookupFailure @Inject()(sessionState: DisposalOfVehicleSessionState) extends Controller {

  import sessionState._

  def present = Action { implicit request =>
    (fetchDealerDetailsFromCache, fetchVehicleLookupDetailsFromCache) match {
      case (Some(dealerDetails), Some(vehicleLookUpFormModelDetails)) => {
        Logger.debug("found dealer and vehicle details")
        val responseCodeErrorMessage: String  = {
          fetchVehicleLookupResponseCodeFromCache match {
            case Some(responseCode) => responseCode
            case _ => "disposal_vehiclelookupfailure.p1"
          }
        }
        clearVehicleLookupResponseCodeFromCache
        Ok(views.html.disposal_of_vehicle.vehicle_lookup_failure(vehicleLookUpFormModelDetails, responseCodeErrorMessage))
      }
      case _ => Redirect(routes.SetUpTradeDetails.present)
    }
  }

  def submit = Action { implicit request =>
    (fetchDealerDetailsFromCache, fetchVehicleLookupDetailsFromCache) match {
      case (Some(dealerDetails), Some(vehicleLookUpFormModelDetails)) => {
        Logger.debug("found dealer and vehicle details")
        Redirect(routes.VehicleLookup.present)
      }
      case _ => Redirect(routes.BeforeYouStart.present)
    }
  }
}


