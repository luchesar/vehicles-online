package controllers.disposal_of_vehicle

import play.api.mvc._
import models.domain.disposal_of_vehicle.{DealerDetailsModel, DisposeViewModel, VehicleDetailsModel}
import scala.Some
import play.api.Logger
import com.google.inject.Inject
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState2.RequestAdapter
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState2.SimpleResultAdapter

class DisposeFailure @Inject()(sessionState: DisposalOfVehicleSessionState) extends Controller {

  import sessionState._

  def present = Action { implicit request =>
    (request.fetch[DealerDetailsModel], fetchDisposeFormModelFromCache, fetchVehicleDetailsFromCache, fetchDisposeTransactionIdFromCache) match {
      case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails), Some(transactionId)) => {
        val disposeModel = fetchData(dealerDetails, vehicleDetails, Some(transactionId))
        Ok(views.html.disposal_of_vehicle.dispose_failure(disposeModel, disposeFormModel))
      }
      case _ =>
        Logger.debug("could not find all expected data in cache on dispose failure present - now redirecting...")
        Redirect(routes.SetUpTradeDetails.present)
    }
  }

  def submit = Action { implicit request =>
    (request.fetch[DealerDetailsModel], fetchDisposeFormModelFromCache, fetchVehicleDetailsFromCache) match {
      case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails)) => Redirect(routes.VehicleLookup.present)
      case _ => Redirect(routes.SetUpTradeDetails.present)
    }
  }

  private def fetchData(dealerDetails: DealerDetailsModel, vehicleDetails: VehicleDetailsModel, transactionId: Option[String]): DisposeViewModel = {
    DisposeViewModel(
      registrationNumber = vehicleDetails.registrationNumber,
      vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      dealerName = dealerDetails.dealerName,
      dealerAddress = dealerDetails.dealerAddress,
      transactionId = transactionId
    )
  }
}
