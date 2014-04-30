package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.Logger
import mappings.common.{ReferenceNumber, RegistrationNumber}
import ReferenceNumber._
import RegistrationNumber._
import mappings.disposal_of_vehicle.VehicleLookup._
import models.domain.disposal_of_vehicle.{VehicleDetailsDto, VehicleDetailsRequest, VehicleDetailsModel, VehicleLookupFormModel}
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import com.google.inject.Inject
import services.vehicle_lookup.VehicleLookupService
import utils.helpers.FormExtensions._

class VehicleLookup @Inject()(sessionState: DisposalOfVehicleSessionState, webService: VehicleLookupService) extends Controller {

  import sessionState._

  val vehicleLookupForm = Form(
    mapping(
      referenceNumberId -> referenceNumber,
      registrationNumberId -> registrationNumber
    )(VehicleLookupFormModel.apply)(VehicleLookupFormModel.unapply)
  )

  def present = Action {
    implicit request =>
      fetchDealerDetailsFromCache match {
        case Some(dealerDetails) => Ok(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, vehicleLookupForm))
        case None => Redirect(routes.SetUpTradeDetails.present)
      }
  }

  def submit = Action.async {
    implicit request =>
      vehicleLookupForm.bindFromRequest.fold(
        formWithErrors =>
          Future {
            fetchDealerDetailsFromCache match {
              case Some(dealerDetails) => val formWithReplacedErrors = formWithErrors.
                  replaceError(registrationNumberId, FormError(key = registrationNumberId, message = "error.restricted.validVRNOnly", args = Seq.empty)).
                  replaceError(referenceNumberId, FormError(key = referenceNumberId, message = "error.validDocumentReferenceNumber", args = Seq.empty)).
                  distinctErrors
                BadRequest(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, formWithReplacedErrors))
              case None => Redirect(routes.SetUpTradeDetails.present)
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
      fetchDealerDetailsFromCache match {
        case Some(dealerDetails) =>
          if (dealerDetails.dealerAddress.uprn.isDefined) Redirect(routes.BusinessChooseYourAddress.present)
          else Redirect(routes.EnterAddressManually.present)
        case None => Redirect(routes.SetUpTradeDetails.present)
      }
  }

  private def lookupVehicle(webService: VehicleLookupService, model: VehicleLookupFormModel): Future[SimpleResult] = {
    webService.invoke(buildMicroServiceRequest(model)).map {
      response =>
        Logger.debug(s"VehicleLookup Web service call successful - response = $response")
        storeVehicleLookupFormModelInCache(model) // TODO Don't save these two models, instead we need a combined model that has what the user entered into the form plus the micro-service response.

        response._1 match {
          case OK => response._2 match {
            case Some(response) => response.responseCode match {
              case Some(responseCode) => vehicleLookupFailurePageWithResponseCode(responseCode)
              case None => response.vehicleDetailsDto match {
                case Some(dto) => disposePageWithVehicleDetailsDto(dto)
                case None => Redirect(routes.MicroServiceError.present)
              }
            }
            case _ => Redirect(routes.MicroServiceError.present)
          }
          case _ => Redirect(routes.VehicleLookupFailure.present)
        }
    }.recover {
      case exception: Throwable => throwToMicroServiceError(exception)
    }
  }

  private def buildMicroServiceRequest(formModel: VehicleLookupFormModel): VehicleDetailsRequest = {
    VehicleDetailsRequest(referenceNumber = formModel.referenceNumber, registrationNumber = formModel.registrationNumber)
  }
  private def throwToMicroServiceError(exception: Throwable) = {
    Logger.debug(s"Web service call failed. Exception: $exception")
    BadRequest("The remote server didn't like the request.")
    Redirect(routes.MicroServiceError.present)
  }

  private def vehicleLookupFailurePageWithResponseCode(responseCode: String) = {
    storeVehicleLookupResponseCodeInCache(responseCode)
    Redirect(routes.VehicleLookupFailure.present)
  }

  private def disposePageWithVehicleDetailsDto(dto: VehicleDetailsDto) = {
    storeVehicleDetailsInCache(VehicleDetailsModel.fromDto(dto))
    Redirect(routes.Dispose.present)
  }
}

