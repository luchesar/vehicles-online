package controllers.disposal_of_vehicle

import play.api.Logger
import play.api.mvc._
import com.google.inject.Inject
import models.domain.disposal_of_vehicle.{TraderDetailsModel, VehicleLookupFormModel}
import mappings.disposal_of_vehicle.VehicleLookup._
import common.{ClientSideSessionFactory, EncryptedCookieImplicits}
import EncryptedCookieImplicits.RequestAdapter
import utils.helpers.{CookieNameHashing, CookieEncryption}

final class VehicleLookupFailure @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  def present = Action { implicit request =>
    (request.getEncryptedCookie[TraderDetailsModel], request.getEncryptedCookie[VehicleLookupFormModel]) match {
      case (Some(dealerDetails), Some(vehicleLookUpFormModelDetails)) =>
        displayVehicleLookupFailure(vehicleLookUpFormModelDetails)
      case _ => Redirect(routes.SetUpTradeDetails.present())
    }
  }

  def submit = Action { implicit request =>
    (request.getEncryptedCookie[TraderDetailsModel], request.getEncryptedCookie[VehicleLookupFormModel]) match {
      case (Some(dealerDetails), Some(vehicleLookUpFormModelDetails)) =>
        Logger.debug("found dealer and vehicle details")
        Redirect(routes.VehicleLookup.present())
      case _ => Redirect(routes.BeforeYouStart.present())
    }
  }

  private def displayVehicleLookupFailure(vehicleLookUpFormModelDetails: VehicleLookupFormModel)(implicit request: Request[AnyContent]) = {
    val responseCodeErrorMessage = encodeResponseCodeErrorMessage
    Ok(views.html.disposal_of_vehicle.vehicle_lookup_failure(vehicleLookUpFormModelDetails, responseCodeErrorMessage)).
      discardingCookies(DiscardingCookie(name = VehicleLookupResponseCodeCacheKey))
  }

  private def encodeResponseCodeErrorMessage(implicit request: Request[AnyContent]): String =
    request.getCookieNamed(VehicleLookupResponseCodeCacheKey) match {
      case Some(responseCode) => responseCode
      case _ => "disposal_vehiclelookupfailure.p1"
    }
}
