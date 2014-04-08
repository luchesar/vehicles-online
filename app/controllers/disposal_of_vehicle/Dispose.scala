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
import scala.annotation.tailrec
import mappings.disposal_of_vehicle.Dispose.dateOfDisposalMaxOffsetIntoThePast
import scala.language.postfixOps

class Dispose @Inject()(webService: DisposeService, dateService: DateService) extends Controller {
  val disposeForm = Form(
    mapping(
      mileageId -> mileage(),
      dateOfDisposalId -> dayMonthYear.verifying(validDate,
        after(earliest = dateService.today - dateOfDisposalMaxOffsetIntoThePast years),
        notInFuture(dateService)),
      emailAddressId -> optional(text),
      consentId -> consent
    )(DisposeFormModel.apply)(DisposeFormModel.unapply)
  )

  private val years: Seq[(String, String)] = models.DayMonthYear.yearsToDropdown(yearNow = dateService.today.year, offsetIntoThePast = dateOfDisposalMaxOffsetIntoThePast)

  def present = Action { implicit request => {
      fetchDealerDetailsFromCache match {
        case (Some(dealerDetails)) =>
          Logger.debug("found dealer details")
          // Pre-populate the form so that the consent checkbox is ticked and today's date is displayed in the date control
          fetchVehicleDetailsFromCache match {
            case (Some(vehicleDetails)) => Ok(views.html.disposal_of_vehicle.dispose(populateModelFromCachedData(dealerDetails, vehicleDetails), disposeForm, years))
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
                replaceError("dateOfDisposal.day", "error.number", FormError("dateOfDisposal.day", "error.dropDownInvalid")).
                replaceError("dateOfDisposal.month", "error.number", FormError("dateOfDisposal.month", "error.dropDownInvalid")).
                replaceError("dateOfDisposal.year", "error.number", FormError("dateOfDisposal.year", "error.date.invalidYear")).
                replaceError(consentId, "error.required", FormError(key = consentId, message = "disposal_dispose.consent.mandatory", args = Seq.empty))
              BadRequest(views.html.disposal_of_vehicle.dispose(disposeViewModel, formWithReplacedErrors, years))
            case _ =>
              Logger.debug("could not find dealer details in cache on Dispose submit")
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
      keeperName = vehicleDetails.keeperName,
      keeperAddress = vehicleDetails.keeperAddress,
      dealerName = dealerDetails.dealerName,
      dealerAddress = dealerDetails.dealerAddress)
    Logger.debug(s"Dispose page read the following data from cache: $model")
    model
  }

  private def disposeAction(webService: DisposeService, f: DisposeFormModel): Future[SimpleResult] = {
    fetchVehicleLookupDetailsFromCache match {
      case Some(vehicleLookupFormModel) =>
        val disposeModel = DisposeModel(referenceNumber = vehicleLookupFormModel.referenceNumber,registrationNumber = vehicleLookupFormModel.registrationNumber, 
          dateOfDisposal = f.dateOfDisposal, mileage = f.mileage)
        storeDisposeModelInCache(disposeModel)
        webService.invoke(buildDisposeMicroServiceRequest(disposeModel)).map {
          resp => Logger.debug(s"Dispose Web service call successful - response = $resp")
          if (resp.success) {
            storeDisposeTransactionIdInCache(resp.transactionId)
            storeDisposeRegistrationNumberInCache(resp.registrationNumber)
            Redirect(routes.DisposeSuccess.present)
          }
          else Redirect(routes.DisposeFailure.present)
        }.recover {
          case e: Throwable =>
            Logger.debug(s"Dispose Web service call failed. Exception: $e")
            BadRequest("The remote server didn't like the request.") // TODO check with BAs what we want to display when the webservice throws exception. We cannot proceed so need to say something like "".
        }
      case _ => Future {
        Logger.debug("could not find dealer details in cache on Dispose submit")
        Redirect(routes.SetUpTradeDetails.present)
      }
    }
  }

  private def buildDisposeMicroServiceRequest(disposeModel: DisposeModel):DisposeRequest = {
    val dateTime = disposeModel.dateOfDisposal.toDateTime.get
    val formatter = ISODateTimeFormat.dateTime()
    val isoDateTimeString = formatter.print(dateTime)
    DisposeRequest(referenceNumber = disposeModel.referenceNumber, registrationNumber = disposeModel.registrationNumber, dateOfDisposal = isoDateTimeString, mileage = disposeModel.mileage)
  }
}