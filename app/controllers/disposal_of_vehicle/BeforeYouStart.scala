package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.Logger
import mappings.disposal_of_vehicle.Dispose._
import mappings.disposal_of_vehicle.VehicleLookup._
import mappings.disposal_of_vehicle.SetupTradeDetails._
import mappings.disposal_of_vehicle.DealerDetails._
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import utils.helpers.CryptoHelper

class BeforeYouStart extends Controller {

  def present = Action { implicit request =>

    Ok(views.html.disposal_of_vehicle.before_you_start()).withNewSession.
      discardingCookies(
        DiscardingCookie(name = CryptoHelper.encryptCookieName(SetupTradeDetailsCacheKey)),
        DiscardingCookie(name = CryptoHelper.encryptCookieName(dealerDetailsCacheKey)),
        DiscardingCookie(name = CryptoHelper.encryptCookieName(businessChooseYourAddressCacheKey)),
        DiscardingCookie(name = CryptoHelper.encryptCookieName(vehicleLookupDetailsCacheKey)),
        DiscardingCookie(name = CryptoHelper.encryptCookieName(vehicleLookupResponseCodeCacheKey)),
        DiscardingCookie(name = CryptoHelper.encryptCookieName(vehicleLookupFormModelCacheKey)),
        DiscardingCookie(name = CryptoHelper.encryptCookieName(disposeFormModelCacheKey)),
        DiscardingCookie(name = CryptoHelper.encryptCookieName(disposeFormTransactionIdCacheKey)),
        DiscardingCookie(name = CryptoHelper.encryptCookieName(disposeFormTimestampIdCacheKey)),
        DiscardingCookie(name = CryptoHelper.encryptCookieName(disposeFormRegistrationNumberCacheKey)),
        DiscardingCookie(name = CryptoHelper.encryptCookieName(disposeModelCacheKey))
      )
  }

  def submit = Action { implicit request =>
    val modelId = request.session.get("modelId")
    Logger.debug(s"BeforeYouStart - reading modelId from session: $modelId")
    Redirect(routes.SetUpTradeDetails.present)
  }
}
