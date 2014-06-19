package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.ClientSideSessionFactory
import common.CookieImplicits.RequestCookiesAdapter
import common.CookieImplicits.SimpleResultAdapter
import mappings.common.Interstitial._
import mappings.common.AlternateLanguages._
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
          Ok(views.html.disposal_of_vehicle.dispose_success(disposeModel, disposeFormModel)).
            discardingCookies(RelatedCacheKeys.DisposeOnlySet) // TODO US320 test for this
        case _ => Redirect(routes.VehicleLookup.present()) // US320 the user has pressed back button after being on dispose-success and pressing new dispose.
      }
  }

  def newDisposal = Action { implicit request =>
    (request.cookies.getModel[TraderDetailsModel], request.cookies.getModel[VehicleDetailsModel]) match {
      case (Some(dealerDetails), Some(vehicleDetails)) =>
        Redirect(routes.Interstitial.present()).
          discardingCookies(RelatedCacheKeys.DisposeSet).
          withCookie(InterstitialCacheKey, routes.VehicleLookup.present().url)
      case _ => Redirect(routes.SetUpTradeDetails.present())
    }
  }

  def exit = Action { implicit request =>
    Redirect(routes.Interstitial.present()).
      discardingCookies(RelatedCacheKeys.FullSet).
      withCookie(InterstitialCacheKey, routes.BeforeYouStart.present().url)
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
