package controllers.disposal_of_vehicle

import com.google.inject.Inject
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState.RequestAdapter
import mappings.disposal_of_vehicle.Dispose._
import mappings.disposal_of_vehicle.VehicleLookup._
import models.domain.disposal_of_vehicle.DisposeViewModel
import models.domain.disposal_of_vehicle.{DisposeFormModel, VehicleDetailsModel, TraderDetailsModel}
import play.api.mvc._
import utils.helpers.CryptoHelper

class DisposeSuccess @Inject()() extends Controller {

  def present = Action {
    implicit request =>
      (request.getCookie[TraderDetailsModel], request.getCookie[DisposeFormModel], request.getCookie[VehicleDetailsModel], request.getCookieNamed(disposeFormTransactionIdCacheKey), request.getCookieNamed(disposeFormRegistrationNumberCacheKey)) match {
        case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails), Some(transactionId), Some(registrationNumber)) =>
          val disposeModel = fetchData(dealerDetails, vehicleDetails, Some(transactionId), registrationNumber)
          Ok(views.html.disposal_of_vehicle.dispose_success(disposeModel, disposeFormModel))
        case _ => Redirect(routes.SetUpTradeDetails.present())
      }
  }

  def newDisposal = Action {
    implicit request =>
      (request.getCookie[TraderDetailsModel], request.getCookie[DisposeFormModel], request.getCookie[VehicleDetailsModel]) match {
        case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails)) => Redirect(routes.VehicleLookup.present()).
          discardingCookies(getCookiesToDiscard: _*)
        case _ => Redirect(routes.SetUpTradeDetails.present())
      }
  }

  private def getCookiesToDiscard: Seq[DiscardingCookie] = {
    val cookieNames = Seq(vehicleLookupDetailsCacheKey,
      vehicleLookupResponseCodeCacheKey,
      vehicleLookupFormModelCacheKey,
      disposeFormModelCacheKey,
      disposeFormTransactionIdCacheKey,
      disposeFormTimestampIdCacheKey,
      disposeFormRegistrationNumberCacheKey,
      disposeModelCacheKey)
    cookieNames.map(cookieName => DiscardingCookie(name = CryptoHelper.encryptCookieName(cookieName)))
  }

  private def fetchData(dealerDetails: TraderDetailsModel, vehicleDetails: VehicleDetailsModel, transactionId: Option[String], registrationNumber: String): DisposeViewModel = {
    DisposeViewModel(vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      dealerName = dealerDetails.traderName,
      dealerAddress = dealerDetails.traderAddress,
      transactionId = transactionId,
      registrationNumber = registrationNumber)
  }
}
