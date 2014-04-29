package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.Logger
import mappings.common.{ReferenceNumber, RegistrationNumber}
import ReferenceNumber._
import RegistrationNumber._
import mappings.disposal_of_vehicle.VehicleLookup._
import models.domain.disposal_of_vehicle.{VehicleDetailsRequest, VehicleDetailsModel, VehicleLookupFormModel}
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

  def present = Action { implicit request =>
    fetchDealerDetailsFromCache match {
      case Some(dealerDetails) => Ok(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, vehicleLookupForm))
      case None => Redirect(routes.SetUpTradeDetails.present)
    }
  }

  def submit = Action.async { implicit request =>
    vehicleLookupForm.bindFromRequest.fold(
      formWithErrors =>
        Future {
          fetchDealerDetailsFromCache match {
            case Some(dealerDetails) =>
              val formWithReplacedErrors = formWithErrors.
                replaceError(registrationNumberId, FormError(key = registrationNumberId, message = "error.restricted.validVRNOnly", args = Seq.empty)).
                replaceError(referenceNumberId, FormError(key = referenceNumberId, message = "error.validDocumentReferenceNumber", args = Seq.empty)).
                distinctErrors
              BadRequest(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, formWithReplacedErrors))
            case None => Redirect(routes.SetUpTradeDetails.present)
          }
        },
      f => {
        val modelWithoutSpaces = f.copy(registrationNumber = f.registrationNumber.replace(" ","")) // DE7 Strip spaces from input as it is not allowed in the micro-service.
        lookupVehicle(webService, modelWithoutSpaces)
      }
    )
  }

  def back = Action { implicit request =>
    fetchDealerDetailsFromCache match {
      case Some(dealerDetails) =>
        if (dealerDetails.dealerAddress.uprn.isDefined) Redirect(routes.BusinessChooseYourAddress.present)
        else Redirect(routes.EnterAddressManually.present)
      case None => Redirect(routes.SetUpTradeDetails.present)
    }
  }

  private def lookupVehicle(webService: VehicleLookupService, model: VehicleLookupFormModel): Future[SimpleResult] = {

    webService.invoke(buildMicroServiceRequest(model)).map { response =>
      Logger.debug(s"VehicleLookup Web service call successful - response = $response")
      // TODO Don't save these two models, instead we need a combined model that has what the user entered into the form plus the micro-service response.
      storeVehicleLookupFormModelInCache(model)

    response._1 match {
      case OK => response._2 match {
        case Some(r) => r.responseCode match {
          case Some(rc) => {
            storeVehicleLookupResponseCodeInCache(rc)
            Redirect (routes.VehicleLookupFailure.present)
          }
          case None => r.vehicleDetailsDto match {
            case Some(dto) => {
              storeVehicleDetailsInCache(VehicleDetailsModel.fromDto(dto))
              Redirect (routes.Dispose.present)
            }
            case None => {
              Redirect(routes.MicroServiceError.present)
            }
          }
        }
        case _ => { //ToDo revisit case _, may not be required however warnings are produced if removed
          Redirect(routes.MicroServiceError.present)
        }
      }
      case _ => Redirect(routes.VehicleLookupFailure.present)
    }

    }.recover {
      case e: Throwable => {
        Logger.debug(s"Web service call failed. Exception: $e")
        BadRequest("The remote server didn't like the request.")
        Redirect(routes.MicroServiceError.present)
      }
    }
  }

  private def buildMicroServiceRequest(formModel: VehicleLookupFormModel):VehicleDetailsRequest = {
    VehicleDetailsRequest(referenceNumber = formModel.referenceNumber, registrationNumber = formModel.registrationNumber)
  }
}
