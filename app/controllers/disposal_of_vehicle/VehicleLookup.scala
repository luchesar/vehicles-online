package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import mappings.common.{DocumentReferenceNumber, VehicleRegistrationNumber}
import DocumentReferenceNumber._
import VehicleRegistrationNumber._
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
import services.brute_force_prevention.BruteForcePreventionService
import common.{ClientSideSessionFactory, CookieImplicits}
import CookieImplicits.RequestCookiesAdapter
import CookieImplicits.SimpleResultAdapter
import CookieImplicits.FormAdapter
import models.domain.common.BruteForcePreventionResponse._
import mappings.disposal_of_vehicle.Logging

final class VehicleLookup @Inject()(bruteForceService: BruteForcePreventionService, vehicleLookupService: VehicleLookupService)
                                   (implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  private[disposal_of_vehicle] val form = Form(
    mapping(
      DocumentReferenceNumberId -> referenceNumber,
      VehicleRegistrationNumberId -> registrationNumber
    )(VehicleLookupFormModel.apply)(VehicleLookupFormModel.unapply)
  )

  def present = Action {
    implicit request =>
      request.cookies.getModel[TraderDetailsModel] match {
        case Some(traderDetails) => Ok(views.html.disposal_of_vehicle.vehicle_lookup(traderDetails, form.fill()))
        case None => Redirect(routes.SetUpTradeDetails.present())
      }
  }

  def submit = Action.async {
    implicit request =>
      form.bindFromRequest.fold(
        formWithErrors =>
          Future {
            request.cookies.getModel[TraderDetailsModel] match {
              case Some(dealerDetails) => val formWithReplacedErrors = formWithErrors.
                replaceError(VehicleRegistrationNumberId, FormError(key = VehicleRegistrationNumberId, message = "error.restricted.validVRNOnly", args = Seq.empty)).
                replaceError(DocumentReferenceNumberId, FormError(key = DocumentReferenceNumberId, message = "error.validDocumentReferenceNumber", args = Seq.empty)).
                distinctErrors
                BadRequest(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, formWithReplacedErrors))
              case None => Redirect(routes.SetUpTradeDetails.present())
            }
          },
        f => {
          checkPermissionToLookup(convertToUpperCaseAndRemoveSpaces(f)) {
            lookupVehicle
          }
        }
      )
  }

  private def convertToUpperCaseAndRemoveSpaces(model: VehicleLookupFormModel) : VehicleLookupFormModel =
    model.copy(registrationNumber = model.registrationNumber.replace(" ", "")
      .toUpperCase)

  def back = Action {
    implicit request =>
      request.cookies.getModel[TraderDetailsModel] match {
        case Some(dealerDetails) =>
          if (dealerDetails.traderAddress.uprn.isDefined) Redirect(routes.BusinessChooseYourAddress.present())
          else Redirect(routes.EnterAddressManually.present())
        case None => Redirect(routes.SetUpTradeDetails.present())
      }
  }

  private def checkPermissionToLookup(formModel: VehicleLookupFormModel)(lookupVehicleFunc: ((VehicleLookupFormModel, BruteForcePreventionViewModel) => Future[SimpleResult]))
                                     (implicit request: Request[_]): Future[SimpleResult] =
    bruteForceService.isVrmLookupPermitted(formModel.registrationNumber).map { response =>
      response // TODO US270 @Lawrence please code review the way we are using map, the lambda (I think we could use _ but it looks strange to read) and flatmap
    } flatMap {
      case Some(bruteForcePreventionViewModel) =>
        // US270: The security micro-service will return a Forbidden (403) message when the vrm is locked, we have hidden that logic as a boolean.
        if (bruteForcePreventionViewModel.permitted) lookupVehicleFunc(formModel, bruteForcePreventionViewModel)
        else Future {
          val registrationNumber = Logging.anonymize(formModel.registrationNumber)
          Logger.warn(s"BruteForceService locked out vrm: $registrationNumber")
          Redirect(routes.VrmLocked.present()).
            withCookie(bruteForcePreventionViewModel)
        }
      case None => Future {
        Redirect(routes.MicroServiceError.present())
      }
    } recover {
      case exception: Throwable =>
        Logger.error(s"Exception thrown by BruteForceService so for safety we won't let anyone through. Exception ${exception.getStackTraceString}")
        Redirect(routes.MicroServiceError.present())
    }

  private def lookupVehicle(model: VehicleLookupFormModel, bruteForcePreventionViewModel: BruteForcePreventionViewModel)(implicit request: Request[_]): Future[SimpleResult] = {
    def lookupSuccess(vehicleDetailsDto: VehicleDetailsDto) =
      Redirect(routes.Dispose.present()).
        withCookie(VehicleDetailsModel.fromDto(vehicleDetailsDto))

    def hasVehicleDetails(vehicleDetailsDto: Option[VehicleDetailsDto])(implicit request: Request[_]) = vehicleDetailsDto match {
      case Some(dto) => lookupSuccess(dto)
      case None => Redirect(routes.MicroServiceError.present())
    }

    def lookupHadProblem(responseCode: String) = {
      Logger.debug("VehicleLookup encountered a problem, redirect to VehicleLookupFailure")
      Redirect(routes.VehicleLookupFailure.present()).
        withCookie(key = VehicleLookupResponseCodeCacheKey, value = responseCode)
    }

    def hasResponseCode(vehicleDetailsResponse: VehicleDetailsResponse)(implicit request: Request[_]) =
      vehicleDetailsResponse.responseCode match {
        case Some(responseCode) => lookupHadProblem(responseCode) // There is only a response code when there is a problem.
        case None => hasVehicleDetails(vehicleDetailsResponse.vehicleDetailsDto) // Happy path when there is no response code therefore no problem.
      }

    def hasVehicleDetailsResponse(vehicleDetailsResponse: Option[VehicleDetailsResponse])(implicit request: Request[_]) =
      vehicleDetailsResponse match {
        case Some(response) => hasResponseCode(response)
        case _ => Redirect(routes.MicroServiceError.present()) // TODO write test to achieve code coverage.
      }

    def isReponseStatusOk(responseStatusVehicleLookupMS: Int,
                          response: Option[VehicleDetailsResponse])(implicit request: Request[_]) =
      responseStatusVehicleLookupMS match {
        case OK => hasVehicleDetailsResponse(response)
        case _ =>
          Logger.error(s"VehicleLookup web service call http status not OK, it was: $responseStatusVehicleLookupMS. Problem may come from either vehicle-lookup micro-service or the VSS")
          Redirect(routes.MicroServiceError.present())
      }

    val vehicleDetailsRequest = VehicleDetailsRequest(
      referenceNumber = model.referenceNumber,
      registrationNumber = model.registrationNumber,
      trackingId = request.cookies.trackingId(),
      userName = request.cookies.getModel[TraderDetailsModel] match {
        case Some(traderDetails) => traderDetails.traderName
        case _ => ""
      }
    )
    vehicleLookupService.invoke(vehicleDetailsRequest).map {
      case (responseStatusVehicleLookupMS: Int, response: Option[VehicleDetailsResponse]) =>
        Logger.debug(s"VehicleLookup micro-service call successful") // - response = $response")
        import models.domain.disposal_of_vehicle.BruteForcePreventionViewModel._
        isReponseStatusOk(responseStatusVehicleLookupMS = responseStatusVehicleLookupMS,
          response = response).
          withCookie(model).
          withCookie(bruteForcePreventionViewModel)
    }.recover {
      case e: Throwable =>
        Logger.debug(s"VehicleLookup Web service call failed. Exception " + e.toString.take(45))
        Redirect(routes.MicroServiceError.present())
    }
  }
}
