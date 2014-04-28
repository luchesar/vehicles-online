package controllers.disposal_of_vehicle

import play.api.data.{Form}
import play.api.data.Forms._
import play.api.Logger
import play.api.mvc._
import mappings.disposal_of_vehicle.Dispose._
import mappings.common.Mileage._
import mappings.common.DayMonthYear.dayMonthYear
import mappings.common.Consent._
import constraints.common
import common.DayMonthYear._
import controllers.disposal_of_vehicle.Helpers._
import models.domain.disposal_of_vehicle._
import com.google.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.FormExtensions._
import models.domain.disposal_of_vehicle.DisposeFormModel
import models.domain.disposal_of_vehicle.VehicleDetailsModel
import org.joda.time.format.{ISODateTimeFormat}
import services.dispose_service.DisposeService
import services.DateService
import models.domain.disposal_of_vehicle.DisposeFormModel
import play.api.data.FormError
import scala.Some
import play.api.mvc.SimpleResult
import models.domain.disposal_of_vehicle.DealerDetailsModel
import models.domain.disposal_of_vehicle.DisposeModel
import models.domain.disposal_of_vehicle.DisposeViewModel
import mappings.disposal_of_vehicle.Dispose.dateOfDisposalYearsIntoThePast
import scala.language.postfixOps

class Dispose @Inject()(webService: DisposeService, dateService: DateService) extends Controller {
  val disposeForm = Form(
    mapping(
      mileageId -> mileage(),
      dateOfDisposalId -> dayMonthYear.verifying(validDate(),
        after(earliest = dateService.today - dateOfDisposalYearsIntoThePast years),
        notInFuture(dateService)),
      consentId -> consent,
      lossOfRegistrationConsentId -> consent
    )(DisposeFormModel.apply)(DisposeFormModel.unapply)
  )

  private val yearsDropdown: Seq[(String, String)] = dateService.today.yearRangeToDropdown(yearsIntoThePast = dateOfDisposalYearsIntoThePast)

  def present = Action { implicit request => {
      fetchDealerDetailsFromCache match {
        case (Some(dealerDetails)) =>
          Logger.debug("found dealer details")
          // Pre-populate the form so that the consent checkbox is ticked and today's date is displayed in the date control
          fetchVehicleDetailsFromCache match {
            case (Some(vehicleDetails)) => Ok(views.html.disposal_of_vehicle.dispose(populateModelFromCachedData(dealerDetails, vehicleDetails), disposeForm, yearsDropdown))
            case _ => Redirect(routes.VehicleLookup.present)
          }
        case _ => Redirect(routes.SetUpTradeDetails.present)
      }
    }
  }

  def submit = Action.async { implicit request =>
    Logger.debug("Submitted dispose form...")
    disposeForm.bindFromRequest.fold(
      formWithErrors =>
        Future {
          (fetchDealerDetailsFromCache, fetchVehicleDetailsFromCache) match {
            case (Some(dealerDetails), Some(vehicleDetails)) =>
              val disposeViewModel = populateModelFromCachedData(dealerDetails, vehicleDetails)
              // When the user doesn't select a value from the drop-down then the mapping will fail to match on an Int before
              // it gets to the constraints, so we need to replace the error type with one that will give a relevant message.
              val formWithReplacedErrors = formWithErrors.
                replaceError("dateOfDisposal.day", FormError("dateOfDisposal", "error.dateOfDisposal")).
                replaceError("dateOfDisposal.month", FormError("dateOfDisposal", "error.dateOfDisposal")).
                replaceError("dateOfDisposal.year", FormError("dateOfDisposal", "error.dateOfDisposal")).
                replaceError("dateOfDisposal", FormError("dateOfDisposal", "error.dateOfDisposal")).
                replaceError(consentId, "error.required", FormError(key = consentId, message = "disposal_dispose.consent.notgiven", args = Seq.empty)).
                replaceError(lossOfRegistrationConsentId, "error.required", FormError(key = lossOfRegistrationConsentId, message = "disposal_dispose.loss_of_registration.consent.notgiven", args = Seq.empty)).
                distinctErrors
              BadRequest(views.html.disposal_of_vehicle.dispose(disposeViewModel, formWithReplacedErrors, yearsDropdown))
            case _ =>
              Logger.debug("could not find expected data in cache on dispose submit - now redirecting...")
              Redirect(routes.SetUpTradeDetails.present)
          }
        },
      f => {
        storeDisposeFormModelInCache(f)
        Logger.debug(s"Dispose form submitted - mileage = ${f.mileage}, disposalDate = ${f.dateOfDisposal}")
        disposeAction(webService, f)
      }
    )
  }

  private def populateModelFromCachedData(dealerDetails: DealerDetailsModel, vehicleDetails: VehicleDetailsModel): DisposeViewModel = {
    val model = DisposeViewModel(
      registrationNumber = vehicleDetails.registrationNumber,
      vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      dealerName = dealerDetails.dealerName,
      dealerAddress = dealerDetails.dealerAddress)
    Logger.debug(s"Dispose page read the following data from cache: $model")
    model
  }

  private def disposeAction(webService: DisposeService, f: DisposeFormModel): Future[SimpleResult] = {
    def callMicroService(disposeModel: DisposeModel) = {
      val disposeRequest = buildDisposeMicroServiceRequest(disposeModel)
      webService.invoke(disposeRequest).map {
        resp => Logger.debug(s"Dispose micro-service call successful - response = $resp")
        storeResponseInCache(resp._2)
        Redirect(Seq(handleResponseCode(responseCode(resp._2)), handleHttpStatusCode(Option(resp._1)), Some(routes.MicroServiceError.present)).flatten.head)
      }.recover {
        case e: Throwable =>
          Logger.warn(s"Dispose micro-service call failed. Exception: $e")
          Redirect(routes.MicroServiceError.present)
      }
    }

    def responseCode (response :Option[DisposeResponse]) = {
      response match {
        case Some(o) => o.responseCode
        case _ => None
      }
    } 
    
    def storeResponseInCache (response :Option[DisposeResponse]) = {
      response match {
        case Some(o) => 
          if (o.transactionId != "") storeDisposeTransactionIdInCache(o.transactionId)
          if (o.registrationNumber != "") storeDisposeRegistrationNumberInCache(o.registrationNumber)
        case _ => 
      }
      transactionTimestamp()
    }

    def transactionTimestamp() = {
      val transactionTimestamp = dateService.today.toDateTime.get
      val formatter = ISODateTimeFormat.dateTime()
      val isoDateTimeString = formatter.print(transactionTimestamp)
      storeDisposeTransactionTimestampInCache(isoDateTimeString)
    }
    
    def buildDisposeMicroServiceRequest(disposeModel: DisposeModel):DisposeRequest = {
      val dateTime = disposeModel.dateOfDisposal.toDateTime.get
      val formatter = ISODateTimeFormat.dateTime()
      val isoDateTimeString = formatter.print(dateTime)
      DisposeRequest(referenceNumber = disposeModel.referenceNumber, registrationNumber = disposeModel.registrationNumber, dateOfDisposal = isoDateTimeString, mileage = disposeModel.mileage)
    }

    def handleResponseCode(disposeResponseCode: Option[String]) = {
      val unableToProcessApplication = "ms.vehiclesService.response.unableToProcessApplication"

      disposeResponseCode match {
        case Some(responseCode) if responseCode == unableToProcessApplication =>
          Logger.warn("Dispose soap endpoint redirecting to dispose failure page...")
          Some(routes.DisposeFailure.present)
        case Some(responseCode) =>
          Logger.warn(s"Dispose micro-service failed: $responseCode, redirecting to error page...")
          Some(routes.MicroServiceError.present)
        case None => None
      }
    }

    def handleHttpStatusCode(StatusCode: Option[Int]) = {
      StatusCode match {
        case Some(OK) => Some(routes.DisposeSuccess.present)
        case Some(SERVICE_UNAVAILABLE) => Some(routes.SoapEndpointError.present)
        case _ => Some(routes.MicroServiceError.present)
      }
    }

    fetchVehicleLookupDetailsFromCache match {
      case Some(vehicleLookupFormModel) =>
        val disposeModel = DisposeModel(referenceNumber = vehicleLookupFormModel.referenceNumber,
          registrationNumber = vehicleLookupFormModel.registrationNumber,
          dateOfDisposal = f.dateOfDisposal, mileage = f.mileage)
        storeDisposeModelInCache(disposeModel)
        callMicroService(disposeModel)
      case _ => Future {
        Logger.error("could not find dealer details in cache on Dispose submit")
        Redirect(routes.SetUpTradeDetails.present)
      }
    }
  }
}