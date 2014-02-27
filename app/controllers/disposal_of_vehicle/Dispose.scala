package controllers.disposal_of_vehicle

import play.api.mvc._
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
import scala.Some
import models.domain.disposal_of_vehicle.DisposeViewModel
import com.google.inject.Inject

class Dispose @Inject() (webService: services.DisposeService)  extends Controller {

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

  def submit = Action {
    implicit request => {
      Logger.debug("Submitted dispose form...")
      disposeForm.bindFromRequest.fold(
        formWithErrors => {
          (fetchDealerDetailsFromCache, fetchVehicleDetailsFromCache) match {
            case (Some(dealerDetails), Some(vehicleDetails)) =>
              val disposeViewModel = fetchData(dealerDetails, vehicleDetails)
              BadRequest(views.html.disposal_of_vehicle.dispose(disposeViewModel, formWithErrors))
            case _ =>
              Logger.error("could not find dealer details in cache on Dispose submit")
              Redirect(routes.SetUpTradeDetails.present)
          }
        },
        f => {
          storeDateOfDisposalInCache(f)
          Logger.debug(s"Dispose form submitted - consent = ${f.consent}, mileage = ${f.mileage}, disposalDate = ${f.dateOfDisposal}")
          Redirect(routes.DisposeSuccess.present)}
      )
    }
  }

  private def fetchData(dealerDetails: DealerDetailsModel, vehicleDetails: VehicleDetailsModel): DisposeViewModel  = {
    DisposeViewModel(vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      keeperName = vehicleDetails.keeperName,
      keeperAddress = vehicleDetails.keeperAddress,
      dealerName = dealerDetails.dealerName,
      dealerAddress = dealerDetails.dealerAddress)
  }

  private def storeDateOfDisposalInCache(f: DisposeFormModel) = {
    val key = mappings.disposal_of_vehicle.Dispose.cacheKey
    val value = f
    play.api.cache.Cache.set(key, value)
    Logger.debug(s"Dispose - stored disposeFromModel in cache: key = $key, value = $f")
  }
}