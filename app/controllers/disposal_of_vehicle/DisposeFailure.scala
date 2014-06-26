package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.ClientSideSessionFactory
import common.CookieImplicits.RichCookies
import mappings.disposal_of_vehicle.Dispose._
import models.domain.disposal_of_vehicle.{DisposeFormModel, DisposeViewModel, TraderDetailsModel, VehicleDetailsModel}
import play.api.Logger
import play.api.mvc._
import utils.helpers.Config

final class DisposeFailure @Inject()(config: Config)(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  def present = Action { implicit request =>
    (request.cookies.getModel[TraderDetailsModel],
      request.cookies.getModel[DisposeFormModel],
      request.cookies.getModel[VehicleDetailsModel],
      request.cookies.getString(DisposeFormTransactionIdCacheKey)) match {
      case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails), Some(transactionId)) =>
        val disposeViewModel = createViewModel(dealerDetails, vehicleDetails, Some(transactionId))
        Ok(views.html.disposal_of_vehicle.dispose_failure(disposeViewModel, disposeFormModel, config.prototypeBannerVisible))
      case _ =>
        Logger.debug("Could not find all expected data in cache on dispose failure present, redirecting")
        Redirect(routes.SetUpTradeDetails.present())
    }
  }

  private def createViewModel(traderDetails: TraderDetailsModel, vehicleDetails: VehicleDetailsModel, transactionId: Option[String]): DisposeViewModel = {
    DisposeViewModel(
      registrationNumber = vehicleDetails.registrationNumber,
      vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      dealerName = traderDetails.traderName,
      dealerAddress = traderDetails.traderAddress,
      transactionId = transactionId
    )
  }
}
