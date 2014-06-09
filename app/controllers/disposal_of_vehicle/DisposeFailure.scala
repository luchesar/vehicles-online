package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.ClientSideSessionFactory
import common.CookieImplicits.RequestCookiesAdapter
import mappings.common.Languages._
import mappings.disposal_of_vehicle.Dispose._
import models.domain.disposal_of_vehicle.DisposeViewModel
import models.domain.disposal_of_vehicle.{DisposeFormModel, TraderDetailsModel, VehicleDetailsModel}
import play.api.Logger
import play.api.Play.current
import play.api.mvc._

final class DisposeFailure @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  def present = Action { implicit request =>
    (request.cookies.getModel[TraderDetailsModel], request.cookies.getModel[DisposeFormModel], request.cookies.getModel[VehicleDetailsModel],

      request.cookies.getString(DisposeFormTransactionIdCacheKey)) match {
      case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails), Some(transactionId)) =>
        val disposeModel = fetchData(dealerDetails, vehicleDetails, Some(transactionId))
        Ok(views.html.disposal_of_vehicle.dispose_failure(disposeModel, disposeFormModel))
      case _ =>
        Logger.debug("Could not find all expected data in cache on dispose failure present, redirecting")
        Redirect(routes.SetUpTradeDetails.present())
    }
  }

  def withLanguageCy = Action { implicit request =>
    Redirect(routes.DisposeFailure.present()).
      withLang(langCy)
  }

  def withLanguageEn = Action { implicit request =>
    Redirect(routes.DisposeFailure.present()).
      withLang(langEn)
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
