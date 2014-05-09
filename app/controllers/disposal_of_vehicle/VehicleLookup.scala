package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import mappings.common.{ReferenceNumber, RegistrationNumber}
import ReferenceNumber._
import RegistrationNumber._
import mappings.disposal_of_vehicle.VehicleLookup._
import models.domain.disposal_of_vehicle._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import com.google.inject.Inject
import services.vehicle_lookup.VehicleLookupService
import utils.helpers.FormExtensions._
import models.domain.disposal_of_vehicle.VehicleLookupFormModel
import play.api.data.FormError
import play.api.mvc.SimpleResult
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState.RequestAdapter
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState.SimpleResultAdapter

class VehicleLookup @Inject()(webService: VehicleLookupService) extends Controller {

  val vehicleLookupForm = Form(
    mapping(
      referenceNumberId -> referenceNumber,
      registrationNumberId -> registrationNumber
    )(VehicleLookupFormModel.apply)(VehicleLookupFormModel.unapply)
  )

  def present = Action {
    implicit request =>
       request.getCookie[TraderDetailsModel] match {
        case Some(dealerDetails) => Ok(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, vehicleLookupForm))
        case None => Redirect(routes.SetUpTradeDetails.present())
      }
  }

  def submit = Action.async {
    implicit request =>
      vehicleLookupForm.bindFromRequest.fold(
        formWithErrors =>
          Future {
            request.getCookie[TraderDetailsModel] match {
              case Some(dealerDetails) => val formWithReplacedErrors = formWithErrors.
                  replaceError(registrationNumberId, FormError(key = registrationNumberId, message = "error.restricted.validVRNOnly", args = Seq.empty)).
                  replaceError(referenceNumberId, FormError(key = referenceNumberId, message = "error.validDocumentReferenceNumber", args = Seq.empty)).
                  distinctErrors
                BadRequest(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, formWithReplacedErrors))
              case None => Redirect(routes.SetUpTradeDetails.present())
            }
          },
        f => {
          val modelWithoutSpaces = f.copy(registrationNumber = f.registrationNumber.replace(" ", "")) // DE7 Strip spaces from input as it is not allowed in the micro-service.
          lookupVehicle(webService, modelWithoutSpaces)
        }
      )
  }

  def back = Action {
    implicit request =>
      request.getCookie[TraderDetailsModel] match {
        case Some(dealerDetails) =>
          if (dealerDetails.traderAddress.uprn.isDefined) Redirect(routes.BusinessChooseYourAddress.present())
          else Redirect(routes.EnterAddressManually.present())
        case None => Redirect(routes.SetUpTradeDetails.present())
      }
  }

  private def lookupVehicle(webService: VehicleLookupService, model: VehicleLookupFormModel): Future[SimpleResult] = {
    webService.invoke(buildMicroServiceRequest(model)).map {
      case (responseStatus: Int, response: Option[VehicleDetailsResponse]) =>
        Logger.debug(s"VehicleLookup Web service call successful - response = $response")
        checkResponseConstruction(responseStatus, response).
          withCookie(model)
    }.recover {
      case exception: Throwable => throwToMicroServiceError(exception)
    }
  }

  private def checkResponseConstruction(responseStatus: Int, response: Option[VehicleDetailsResponse]) = {
    responseStatus match {
      case OK => okResponseConstruction(response)
      case _ => Redirect(routes.VehicleLookupFailure.present())
    }
  }

  private def okResponseConstruction (vehicleDetailsResponse: Option[VehicleDetailsResponse]) = {
    vehicleDetailsResponse match {
      case Some(response) => responseCodePresent(response)
      case _ => Redirect(routes.MicroServiceError.present())
    }
  }
  
  private def responseCodePresent(response: VehicleDetailsResponse) = {
    response.responseCode match {
      case Some(responseCode) =>
        Redirect(routes.VehicleLookupFailure.present()).
          withCookie(key = vehicleLookupResponseCodeCacheKey, value = responseCode) // TODO [SKW] I don't see a controller spec for testing that the correct value was written to the cache. Write one.
      case None => noResponseCodePresent(response.vehicleDetailsDto)
    }
  }

  private def noResponseCodePresent(vehicleDetailsDto: Option[VehicleDetailsDto]) = {
    vehicleDetailsDto match {
      case Some(dto) =>
        Redirect(routes.Dispose.present()).
          withCookie(VehicleDetailsModel.fromDto(dto))
      case None => Redirect(routes.MicroServiceError.present())
    }
  }

  private def buildMicroServiceRequest(formModel: VehicleLookupFormModel): VehicleDetailsRequest = {
    VehicleDetailsRequest(referenceNumber = formModel.referenceNumber, registrationNumber = formModel.registrationNumber)
  }

  private def throwToMicroServiceError(exception: Throwable) = {
    Logger.debug(s"Web service call failed. Exception: $exception")
    BadRequest("The remote server didn't like the request.")
    Redirect(routes.MicroServiceError.present())
  }
}