package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.ClientSideSessionFactory
import common.CookieImplicits.RequestCookiesAdapter
import common.CookieImplicits.SimpleResultAdapter
import mappings.common.Languages._
import mappings.disposal_of_vehicle.Dispose._
import mappings.disposal_of_vehicle.RelatedCacheKeys
import models.domain.disposal_of_vehicle.DisposeViewModel
import models.domain.disposal_of_vehicle.{DisposeFormModel, VehicleDetailsModel, TraderDetailsModel}
import play.api.Play.current
import play.api.mvc._

final class DisposeSuccess @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  def present = Action {
    implicit request =>
      (request.cookies.getModel[TraderDetailsModel], request.cookies.getModel[DisposeFormModel], request.cookies.getModel[VehicleDetailsModel],
        request.cookies.getString(DisposeFormTransactionIdCacheKey), request.cookies.getString(DisposeFormRegistrationNumberCacheKey)) match {
        case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails), Some(transactionId), Some(registrationNumber)) =>
          val disposeModel = fetchData(dealerDetails, vehicleDetails, Some(transactionId), registrationNumber)
          Ok(views.html.disposal_of_vehicle.dispose_success(disposeModel, disposeFormModel))
        case _ => Redirect(routes.VehicleLookup.present()) // US320 the user has pressed back button after being on dispose-success and pressing new dispose.
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

  def withLanguageCy = Action { implicit request =>
    Redirect(routes.DisposeSuccess.present()).
      withLang(langCy)
  }

  def withLanguageEn = Action { implicit request =>
    Redirect(routes.DisposeSuccess.present()).
      withLang(langEn)
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
