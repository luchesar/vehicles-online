package controllers.disposal_of_vehicle

import play.api.Logger
import play.api.mvc._
import scala.Some
import com.google.inject.Inject
import models.domain.disposal_of_vehicle.{DealerDetailsModel, VehicleLookupFormModel}
import mappings.disposal_of_vehicle.VehicleLookup._
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState2.RequestAdapter
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState2.SimpleResultAdapter

class VehicleLookupFailure @Inject()(sessionState: DisposalOfVehicleSessionState) extends Controller {

  import sessionState._

  def present = Action { implicit request =>
    (request.fetch[DealerDetailsModel], request.fetch[VehicleLookupFormModel]) match {
      case (Some(dealerDetails), Some(vehicleLookUpFormModelDetails)) => {
        Logger.debug("found dealer and vehicle details")
        displayVehicleLookupFailure(vehicleLookUpFormModelDetails)
      }
      case _ => Redirect(routes.SetUpTradeDetails.present)
    }
  }

  def submit = Action { implicit request =>
    (request.fetch[DealerDetailsModel], request.fetch[VehicleLookupFormModel]) match {
      case (Some(dealerDetails), Some(vehicleLookUpFormModelDetails)) => {
        Logger.debug("found dealer and vehicle details")
        Redirect(routes.VehicleLookup.present)
      }
      case _ => Redirect(routes.BeforeYouStart.present)
    }
  }

  private def displayVehicleLookupFailure(vehicleLookUpFormModelDetails: VehicleLookupFormModel)(implicit request: Request[AnyContent]) = {
    val responseCodeErrorMessage = encodeResponseCodeErrorMessage
    Ok(views.html.disposal_of_vehicle.vehicle_lookup_failure(vehicleLookUpFormModelDetails, responseCodeErrorMessage)).
      discardingCookies(DiscardingCookie(name = vehicleLookupResponseCodeCacheKey)) // TODO [SKW] please someone write a test for this and make sure it only removes this cookie and no other cookies.
  }

  private def encodeResponseCodeErrorMessage(implicit request: Request[AnyContent]): String =
    request.fetch(vehicleLookupResponseCodeCacheKey) match {
      case Some(responseCode) => responseCode
      case _ => "disposal_vehiclelookupfailure.p1"
    }
}
