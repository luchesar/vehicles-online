package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.{ClientSideSessionFactory, EncryptedCookieImplicits}
import EncryptedCookieImplicits.RequestAdapter
import mappings.disposal_of_vehicle.Dispose._
import models.domain.disposal_of_vehicle.DisposeViewModel
import models.domain.disposal_of_vehicle.{DisposeFormModel, VehicleDetailsModel, TraderDetailsModel}
import play.api.mvc._
import EncryptedCookieImplicits.SimpleResultAdapter
import mappings.disposal_of_vehicle.RelatedCacheKeys

final class DisposeSuccess @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  def present = Action {
    implicit request =>
      (request.getEncryptedCookie[TraderDetailsModel], request.getEncryptedCookie[DisposeFormModel], request.getEncryptedCookie[VehicleDetailsModel],
        request.getCookieNamed(DisposeFormTransactionIdCacheKey), request.getCookieNamed(DisposeFormRegistrationNumberCacheKey)) match {
        case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails), Some(transactionId), Some(registrationNumber)) =>
          val disposeModel = fetchData(dealerDetails, vehicleDetails, Some(transactionId), registrationNumber)
          Ok(views.html.disposal_of_vehicle.dispose_success(disposeModel, disposeFormModel))
        case _ => Redirect(routes.SetUpTradeDetails.present())
      }
  }

  def submit = Action { implicit request =>
    val formData = request.body.asFormUrlEncoded.getOrElse(Map.empty[String, Seq[String]])
    val actionValue = formData.get("action").flatMap(_.headOption)
    actionValue match {
      case Some("newDisposal") =>
        newDisposal
      case Some("exit") =>
        exit
      case _ => BadRequest("This action is not allowed") // TODO redirect to error page ?
    }
  }

  private def newDisposal(implicit request: Request[AnyContent]): SimpleResult =
    (request.getEncryptedCookie[TraderDetailsModel], request.getEncryptedCookie[DisposeFormModel],
      request.getEncryptedCookie[VehicleDetailsModel]) match {
      case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails)) => Redirect(routes.VehicleLookup.present()).
        discardingEncryptedCookies(RelatedCacheKeys.DisposeSet)
      case _ => Redirect(routes.SetUpTradeDetails.present())
    }

  private def exit(implicit request: Request[AnyContent]): SimpleResult = {
      Redirect(routes.BeforeYouStart.present()).discardingEncryptedCookies(RelatedCacheKeys.FullSet)
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
