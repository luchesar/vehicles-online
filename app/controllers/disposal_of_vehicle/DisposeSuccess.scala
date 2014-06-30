package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.ClientSideSessionFactory
import mappings.common.PreventGoingToDisposePage._
import mappings.disposal_of_vehicle.Dispose._
import mappings.disposal_of_vehicle.RelatedCacheKeys
import models.domain.disposal_of_vehicle.{DisposeFormModel, DisposeViewModel, TraderDetailsModel, VehicleDetailsModel}
import play.api.mvc._
import services.DateService
import common.CookieImplicits._
import utils.helpers.Config

final class DisposeSuccess @Inject()(implicit clientSideSessionFactory: ClientSideSessionFactory,
                                     config: Config,
                                     dateService: DateService) extends Controller {

  def present = Action { implicit request =>
    (request.cookies.getModel[TraderDetailsModel],
     request.cookies.getModel[DisposeFormModel],
     request.cookies.getModel[VehicleDetailsModel],
     request.cookies.getString(DisposeFormTransactionIdCacheKey),
     request.cookies.getString(DisposeFormRegistrationNumberCacheKey)) match {
       case (Some(traderDetails),
             Some(disposeFormModel),
             Some(vehicleDetails),
             Some(transactionId),
             Some(registrationNumber)) =>
         val disposeViewModel = createViewModel(
           traderDetails,
           vehicleDetails,
           Some(transactionId),
           registrationNumber
         )
         Ok(views.html.disposal_of_vehicle.dispose_success(disposeViewModel, disposeFormModel, prototypeServeryUrl(request))).
           discardingCookies(RelatedCacheKeys.DisposeOnlySet) // TODO US320 test for this
       case _ => Redirect(routes.VehicleLookup.present()) // US320 the user has pressed back button after being on dispose-success and pressing new dispose.
     }
  }

  def newDisposal = Action { implicit request =>
    (request.cookies.getModel[TraderDetailsModel], request.cookies.getModel[VehicleDetailsModel]) match {
      case (Some(traderDetails), Some(vehicleDetails)) =>
        Redirect(routes.VehicleLookup.present()).
          discardingCookies(RelatedCacheKeys.DisposeSet).
          withCookie(PreventGoingToDisposePageCacheKey, "")
      case _ => Redirect(routes.SetUpTradeDetails.present())
    }
  }

  def exit = Action { implicit request =>
    Redirect(routes.BeforeYouStart.present()).
      discardingCookies(RelatedCacheKeys.FullSet).
      withCookie(PreventGoingToDisposePageCacheKey, "")
  }

  def survey = Action { implicit request =>
    Redirect(Call("GET", config.prototypeSurveyUrl)).
      withCookie(SurveyRequestTriggerDateCacheKey, dateService.now.getMillis.toString)
  }

  private def createViewModel(traderDetails: TraderDetailsModel,
                              vehicleDetails: VehicleDetailsModel,
                              transactionId: Option[String],
                              registrationNumber: String): DisposeViewModel =
    DisposeViewModel(
      vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      dealerName = traderDetails.traderName,
      dealerAddress = traderDetails.traderAddress,
      transactionId = transactionId,
      registrationNumber = registrationNumber
    )

  private def prototypeServeryUrl(request: Request[_]): Option[String] = {
    def url = if (!config.prototypeSurveyUrl.trim.isEmpty)
      Some(config.prototypeSurveyUrl.trim)
    else None

    request.cookies.getString(SurveyRequestTriggerDateCacheKey) match {
      case Some(lastSurveyMillis) =>
        if ((lastSurveyMillis.toLong + config.prototypeSurveyPrepositionInterval) < dateService.now.getMillis) url
        else None
      case None => url
    }
  }
}
