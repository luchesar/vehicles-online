package controllers.disposal_of_vehicle

import play.api.mvc._
import controllers.disposal_of_vehicle.Helpers._
import models.domain.disposal_of_vehicle.{DealerDetailsModel, DisposeViewModel, VehicleDetailsModel}
import scala.Some

object DisposeFailure extends Controller {

  def present = Action {
    implicit request => {
      (fetchDealerDetailsFromCache, fetchDisposeFormModelFromCache, fetchVehicleDetailsFromCache) match {
        case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails)) =>
          val disposeModel = fetchData(dealerDetails, vehicleDetails)
          Ok(views.html.disposal_of_vehicle.dispose_failure(disposeModel, disposeFormModel))
        case _ => Redirect(routes.SetUpTradeDetails.present)
      }
    }
  }

  def submit = Action {
    implicit request => {
      (fetchDealerDetailsFromCache, fetchDisposeFormModelFromCache, fetchVehicleDetailsFromCache) match {
        case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails)) => Redirect(routes.VehicleLookup.present)
        case _ => Redirect(routes.SetUpTradeDetails.present)
      }
    }
  }

  private def fetchData(dealerDetails: DealerDetailsModel, vehicleDetails: VehicleDetailsModel): DisposeViewModel = {
    DisposeViewModel(vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      keeperName = vehicleDetails.keeperName,
      keeperAddress = vehicleDetails.keeperAddress,
      dealerName = dealerDetails.dealerName,
      dealerAddress = dealerDetails.dealerAddress)
  }
}
