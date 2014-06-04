package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.{ClientSideSessionFactory, CookieImplicits}
import CookieImplicits.RequestCookiesAdapter
import mappings.disposal_of_vehicle.Dispose._
import models.domain.disposal_of_vehicle.DisposeViewModel
import models.domain.disposal_of_vehicle.{DisposeFormModel, VehicleDetailsModel, TraderDetailsModel}
import play.api.mvc._
import CookieImplicits.SimpleResultAdapter
import mappings.disposal_of_vehicle.RelatedCacheKeys

final class DisposeSuccess @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  def present = Action {
    implicit request =>
      (request.cookies.getModel[TraderDetailsModel], request.cookies.getModel[DisposeFormModel], request.cookies.getModel[VehicleDetailsModel],
        request.cookies.getString(DisposeFormTransactionIdCacheKey), request.cookies.getString(DisposeFormRegistrationNumberCacheKey)) match {
        case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails), Some(transactionId), Some(registrationNumber)) =>
          val disposeModel = fetchData(dealerDetails, vehicleDetails, Some(transactionId), registrationNumber)
          Ok(views.html.disposal_of_vehicle.dispose_success(disposeModel, disposeFormModel))
        case _ => Redirect(routes.SetUpTradeDetails.present())
      }
  }

  def newDisposal = Action { implicit request =>
    (request.cookies.getModel[TraderDetailsModel], request.cookies.getModel[DisposeFormModel],
      request.cookies.getModel[VehicleDetailsModel]) match {
      case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails)) => Redirect(routes.VehicleLookup.present()).
        discardingCookies(RelatedCacheKeys.DisposeSet)
      case _ => Redirect(routes.SetUpTradeDetails.present())
    }
  }

  def exit = Action { implicit request =>
    Redirect(routes.BeforeYouStart.present()).discardingCookies(RelatedCacheKeys.FullSet)
  }

  private def fetchData(dealerDetails: TraderDetailsModel, vehicleDetails: VehicleDetailsModel, transactionId: Option[String],
                        registrationNumber: String): DisposeViewModel = {
    DisposeViewModel(vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      dealerName = dealerDetails.traderName,
      dealerAddress = dealerDetails.traderAddress,
      transactionId = transactionId,
      registrationNumber = registrationNumber)
  }
}
