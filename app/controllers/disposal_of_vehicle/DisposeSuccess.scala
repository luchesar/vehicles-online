package controllers.disposal_of_vehicle

import play.api.mvc._
import models.domain.disposal_of_vehicle.{VehicleDetailsModel, DealerDetailsModel, DisposeViewModel}
import controllers.disposal_of_vehicle.Helpers._

object DisposeSuccess extends Controller {

  def present = Action { implicit request =>
    (fetchDealerDetailsFromCache, fetchDisposeFormModelFromCache, fetchVehicleDetailsFromCache, fetchDisposeTransactionIdFromCache, fetchDisposeRegistrationNumberFromCache) match {
      case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails), Some(transactionId), Some(registrationNumber)) =>
        val disposeModel = fetchData(dealerDetails, vehicleDetails, Some(transactionId), Some(registrationNumber))
        Ok(views.html.disposal_of_vehicle.dispose_success(disposeModel, disposeFormModel))
      case _ => Redirect(routes.SetUpTradeDetails.present)
    }
  }

  def submit = Action { implicit request =>
    (fetchDealerDetailsFromCache, fetchDisposeFormModelFromCache, fetchVehicleDetailsFromCache) match {
      case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails)) => Redirect(routes.VehicleLookup.present)
      case _ => Redirect(routes.SetUpTradeDetails.present)
    }
  }

  private def fetchData(dealerDetails: DealerDetailsModel, vehicleDetails: VehicleDetailsModel, transactionId: Option[String], registrationNumber: Option[String]): DisposeViewModel = {
    DisposeViewModel(vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      keeperName = vehicleDetails.keeperName,
      keeperAddress = vehicleDetails.keeperAddress,
      dealerName = dealerDetails.dealerName,
      dealerAddress = dealerDetails.dealerAddress,
      transactionId = transactionId,
      registrationNumber = registrationNumber)
  }
}