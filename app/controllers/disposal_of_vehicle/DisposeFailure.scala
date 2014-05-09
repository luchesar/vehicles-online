package controllers.disposal_of_vehicle

import play.api.mvc._
import models.domain.disposal_of_vehicle.{DisposeFormModel, TraderDetailsModel, DisposeViewModel, VehicleDetailsModel}
import scala.Some
import play.api.Logger
import com.google.inject.Inject
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState.RequestAdapter
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState.SimpleResultAdapter
import mappings.disposal_of_vehicle.Dispose._
import scala.Some
import models.domain.disposal_of_vehicle.DisposeViewModel

class DisposeFailure @Inject()() extends Controller {


  def present = Action { implicit request =>
    (request.fetch[TraderDetailsModel], request.fetch[DisposeFormModel], request.fetch[VehicleDetailsModel], request.fetch(disposeFormTransactionIdCacheKey)) match {
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
    (request.fetch[TraderDetailsModel], request.fetch[DisposeFormModel], request.fetch[VehicleDetailsModel]) match {
      case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails)) => Redirect(routes.VehicleLookup.present)
      case _ => Redirect(routes.SetUpTradeDetails.present)
    }
  }

  private def fetchData(dealerDetails: TraderDetailsModel, vehicleDetails: VehicleDetailsModel, transactionId: Option[String]): DisposeViewModel = {
    DisposeViewModel(
      registrationNumber = vehicleDetails.registrationNumber,
      vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      dealerName = dealerDetails.traderName,
      dealerAddress = dealerDetails.traderAddress,
      transactionId = transactionId
    )
  }
}
