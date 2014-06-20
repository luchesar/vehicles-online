package controllers.disposal_of_vehicle

import play.api.Logger
import play.api.mvc._
import com.google.inject.Inject
import models.domain.disposal_of_vehicle.{BruteForcePreventionViewModel, TraderDetailsModel, VehicleLookupFormModel}
import mappings.disposal_of_vehicle.VehicleLookup._
import common.{ClientSideSessionFactory, CookieImplicits}
import CookieImplicits.RichCookies
import mappings.common.AlternateLanguages._
import play.api.mvc.DiscardingCookie
import play.api.Play.current

final class VehicleLookupFailure @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  def present = Action { implicit request =>
    (request.cookies.getModel[TraderDetailsModel], request.cookies.getModel[BruteForcePreventionViewModel], request.cookies.getModel[VehicleLookupFormModel], request.cookies.getString(VehicleLookupResponseCodeCacheKey)) match {
      case (Some(dealerDetails), Some(bruteForcePreventionResponse), Some(vehicleLookUpFormModelDetails), Some(vehicleLookupResponseCode)) =>
        displayVehicleLookupFailure(vehicleLookUpFormModelDetails, bruteForcePreventionResponse, vehicleLookupResponseCode)
      case _ => Redirect(routes.SetUpTradeDetails.present())
    }
  }

  def submit = Action { implicit request =>
    (request.cookies.getModel[TraderDetailsModel], request.cookies.getModel[VehicleLookupFormModel]) match {
      case (Some(dealerDetails), Some(vehicleLookUpFormModelDetails)) =>
        Logger.debug("Found dealer and vehicle details")
        Redirect(routes.VehicleLookup.present())
      case _ => Redirect(routes.BeforeYouStart.present())
    }
  }

  private def displayVehicleLookupFailure(vehicleLookUpFormModelDetails: VehicleLookupFormModel,
                                          bruteForcePreventionViewModel: BruteForcePreventionViewModel,
                                          vehicleLookupResponseCode: String)(implicit request: Request[AnyContent]) = {
    Ok(views.html.disposal_of_vehicle.vehicle_lookup_failure(
      data = vehicleLookUpFormModelDetails,
      responseCodeVehicleLookupMSErrorMessage = vehicleLookupResponseCode,
      attempts = bruteForcePreventionViewModel.attempts,
      maxAttempts = bruteForcePreventionViewModel.maxAttempts)
    ).
    discardingCookies(DiscardingCookie(name = VehicleLookupResponseCodeCacheKey))
  }
}
