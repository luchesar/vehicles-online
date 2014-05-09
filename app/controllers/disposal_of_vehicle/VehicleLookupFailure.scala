package controllers.disposal_of_vehicle

import play.api.Logger
import play.api.mvc._
import com.google.inject.Inject
import models.domain.disposal_of_vehicle.{TraderDetailsModel, VehicleLookupFormModel}
import mappings.disposal_of_vehicle.VehicleLookup._
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState.RequestAdapter

class VehicleLookupFailure @Inject()() extends Controller {


  def present = Action { implicit request =>
    (request.getCookie[TraderDetailsModel], request.getCookie[VehicleLookupFormModel]) match {
      case (Some(dealerDetails), Some(vehicleLookUpFormModelDetails)) =>
        displayVehicleLookupFailure(vehicleLookUpFormModelDetails)
      case _ => Redirect(routes.SetUpTradeDetails.present)
    }
  }

  def submit = Action { implicit request =>
    (request.getCookie[TraderDetailsModel], request.getCookie[VehicleLookupFormModel]) match {
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
    request.getCookieNamed(vehicleLookupResponseCodeCacheKey) match {
      case Some(responseCode) => responseCode
      case _ => "disposal_vehiclelookupfailure.p1"
    }
}
