package controllers.disposal_of_vehicle

import play.api.mvc._
import models.domain.disposal_of_vehicle.{DisposeFormModel, TraderDetailsModel, VehicleDetailsModel}
import play.api.Logger
import com.google.inject.Inject
import common.{ClientSideSessionFactory, EncryptedCookieImplicits}
import EncryptedCookieImplicits.RequestAdapter
import mappings.disposal_of_vehicle.Dispose._
import models.domain.disposal_of_vehicle.DisposeViewModel
import utils.helpers.{CookieNameHashing, CookieEncryption}

final class DisposeFailure @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {


  def present = Action { implicit request =>
    (request.getEncryptedCookie[TraderDetailsModel], request.getEncryptedCookie[DisposeFormModel], request.getEncryptedCookie[VehicleDetailsModel], request.getCookieNamed(DisposeFormTransactionIdCacheKey)) match {
      case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails), Some(transactionId)) =>
        val disposeModel = fetchData(dealerDetails, vehicleDetails, Some(transactionId))
        Ok(views.html.disposal_of_vehicle.dispose_failure(disposeModel, disposeFormModel))
      case _ =>
        Logger.debug("could not find all expected data in cache on dispose failure present - now redirecting...")
        Redirect(routes.SetUpTradeDetails.present())
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
