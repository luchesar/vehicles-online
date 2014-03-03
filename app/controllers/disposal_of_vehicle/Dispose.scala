package controllers.disposal_of_vehicle

import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import mappings.disposal_of_vehicle.Dispose._
import mappings.common.{Mileage, DayMonthYear, Consent}
import Consent._
import Mileage._
import DayMonthYear._
import constraints.DayMonthYear._
import controllers.disposal_of_vehicle.Helpers._
import play.api.Play.current
import models.domain.disposal_of_vehicle.VehicleDetailsModel
import models.domain.disposal_of_vehicle.DealerDetailsModel
import models.domain.disposal_of_vehicle.DisposeFormModel
import models.domain.disposal_of_vehicle.DisposeModel
import scala.Some
import models.domain.disposal_of_vehicle.DisposeViewModel
import com.google.inject.Inject
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import play.api.mvc._

class Dispose @Inject()(webService: services.DisposeService) extends Controller {

  val disposeForm = Form(
    mapping(
      consentId -> consent,
      mileageId -> mileage(),
      dateOfDisposalId -> dayMonthYear.verifying(rules),
      emailAddressId -> optional(text)
    )(DisposeFormModel.apply)(DisposeFormModel.unapply)
  )

  def present = Action {
    implicit request => {
      (fetchDealerDetailsFromCache, fetchVehicleDetailsFromCache) match {
        case (Some(dealerDetails), Some(vehicleDetails)) => {
          Logger.debug("found dealer details")
          // Pre-populate the form so that the consent checkbox is ticked and today's date is displayed in the date control
          //val filledForm = disposeForm.fill(DisposeFormModel(consent = "false", dateOfDisposal = models.DayMonthYear.today))
          Ok(views.html.disposal_of_vehicle.dispose(fetchData(dealerDetails, vehicleDetails), disposeForm))
        }
        case _ => Redirect(routes.SetUpTradeDetails.present)
      }
    }
  }

  def submit = Action.async {
    implicit request => {
      Logger.debug("Submitted dispose form...")
      disposeForm.bindFromRequest.fold(
        formWithErrors => {
          Future {
            (fetchDealerDetailsFromCache, fetchVehicleDetailsFromCache) match {
              case (Some(dealerDetails), Some(vehicleDetails)) =>
                val disposeViewModel = fetchData(dealerDetails, vehicleDetails)
                BadRequest(views.html.disposal_of_vehicle.dispose(disposeViewModel, formWithErrors))
              case _ =>
                Logger.error("could not find dealer details in cache on Dispose submit")
                Redirect(routes.SetUpTradeDetails.present)
            }
          }
        },
        f => {
          storeDateOfDisposalInCache(f)
          Logger.debug(s"Dispose form submitted - consent = ${f.consent}, mileage = ${f.mileage}, disposalDate = ${f.dateOfDisposal}")
          fetchVehicleLookupDetailsFromCache match {
            //TODO could be moved inside disposeAction
            case Some(vehicleLookupFormModel) => {
              val disposeModel = DisposeModel(v5cReferenceNumber = vehicleLookupFormModel.v5cReferenceNumber, v5cRegistrationNumber = vehicleLookupFormModel.v5cRegistrationNumber, v5cKeeperName = vehicleLookupFormModel.v5cKeeperName, v5cPostcode = vehicleLookupFormModel.v5cPostcode)
              disposeAction(webService, disposeModel)
            }
            case _ => Future {
              Logger.error("could not find dealer details in cache on Dispose submit")
              Redirect(routes.SetUpTradeDetails.present)
            }
          }
        }

      )
    }
  }

  private def fetchData(dealerDetails: DealerDetailsModel, vehicleDetails: VehicleDetailsModel): DisposeViewModel = {
    DisposeViewModel(vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      keeperName = vehicleDetails.keeperName,
      keeperAddress = vehicleDetails.keeperAddress,
      dealerName = dealerDetails.dealerName,
      dealerAddress = dealerDetails.dealerAddress,
      transactionId = Some("123-abc"))
  }

  private def storeDateOfDisposalInCache(f: DisposeFormModel) = {
    val key = mappings.disposal_of_vehicle.Dispose.cacheKey
    val value = f
    play.api.cache.Cache.set(key, value)
    Logger.debug(s"Dispose - stored disposeFromModel in cache: key = $key, value = $f")
  }

  private def disposeAction(webService: services.DisposeService, model: DisposeModel): Future[SimpleResult] = {
    webService.invoke(model).map {
      resp =>
        Logger.debug(s"Dispose Web service call successful - response = ${resp}")
        if (resp.success) Redirect(routes.DisposeSuccess.present)
        else Redirect(routes.DisposeFailure.present)
    }.recoverWith {
      case e: Throwable => Future {
        Logger.debug(s"Web service call failed. Exception: ${e}")
        BadRequest("The remote server didn't like the request.") // TODO check with BAs what we want to display when the webservice throws exception. We cannot proceed so need to say something like "".
      }
    }
  }

}