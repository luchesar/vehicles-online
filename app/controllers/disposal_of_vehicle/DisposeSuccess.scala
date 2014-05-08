package controllers.disposal_of_vehicle

import play.api.mvc._
import models.domain.disposal_of_vehicle.{DisposeFormModel, VehicleDetailsModel, DealerDetailsModel, DisposeViewModel}
import com.google.inject.Inject
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState.RequestAdapter
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState.SimpleResultAdapter
import mappings.disposal_of_vehicle.Dispose._
import scala.Some
import models.domain.disposal_of_vehicle.DisposeViewModel

class DisposeSuccess @Inject()() extends Controller {

  def present = Action { implicit request =>
    (request.fetch[DealerDetailsModel], request.fetch[DisposeFormModel], request.fetch[VehicleDetailsModel], request.fetch(disposeFormTransactionIdCacheKey), request.fetch(disposeFormRegistrationNumberCacheKey)) match {
      case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails), Some(transactionId), Some(registrationNumber)) =>
        val disposeModel = fetchData(dealerDetails, vehicleDetails, Some(transactionId), registrationNumber)
        Ok(views.html.disposal_of_vehicle.dispose_success(disposeModel, disposeFormModel))
      case _ => Redirect(routes.SetUpTradeDetails.present)
    }
  }

  def submit = Action { implicit request =>
    (request.fetch[DealerDetailsModel], request.fetch[DisposeFormModel], request.fetch[VehicleDetailsModel]) match {
      case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails)) => Redirect(routes.VehicleLookup.present)
      case _ => Redirect(routes.SetUpTradeDetails.present)
    }
  }

  private def fetchData(dealerDetails: DealerDetailsModel, vehicleDetails: VehicleDetailsModel, transactionId: Option[String], registrationNumber: String): DisposeViewModel = {
    DisposeViewModel(vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      dealerName = dealerDetails.dealerName,
      dealerAddress = dealerDetails.dealerAddress,
      transactionId = transactionId,
      registrationNumber = registrationNumber)
  }
}
