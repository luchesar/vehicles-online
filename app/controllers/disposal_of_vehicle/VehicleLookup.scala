package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import models.domain.disposal_of_vehicle._
import mappings.disposal_of_vehicle.VehicleLookup._
import mappings.common.V5cReferenceNumber
import V5cReferenceNumber._
import mappings.common.V5cRegistrationNumber
import V5cRegistrationNumber._
import controllers.disposal_of_vehicle.Helpers._
import play.cache.Cache
import models.domain.disposal_of_vehicle.VehicleLookupFormModel
import models.domain.disposal_of_vehicle.VehicleDetailsModel
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import mappings.common.Postcode._
import com.google.inject.Inject
import controllers.disposal_of_vehicle.Helpers.{storeVehicleDetailsInCache,storeVehicleLookupFormModelInCache}

class VehicleLookup @Inject() (webService: services.VehicleLookupService) extends Controller {

  val vehicleLookupForm = Form(
    mapping(
      v5cReferenceNumberId -> v5cReferenceNumber(minLength = 11, maxLength = 11),
      v5cRegistrationNumberId -> v5CRegistrationNumber(minLength = 2, maxLength = 8),
      v5cKeeperNameId -> nonEmptyText(minLength = 2, maxLength = 100),
      v5cPostcodeId -> postcode()
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
        formWithErrors => Future {
          fetchDealerDetailsFromCache match {
            case Some(dealerDetails) => BadRequest(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, formWithErrors))
            case None => Redirect(routes.SetUpTradeDetails.present)
          }
        },
        f =>  lookupVehicle(webService, f)
      )
  }

  private def lookupVehicle(webService: services.VehicleLookupService, model: VehicleLookupFormModel) : Future[SimpleResult] = {
    webService.invoke(model).map { resp =>
      Logger.debug(s"VehicleLookup Web service call successful - response = ${resp}")
      storeVehicleDetailsInCache(resp.vehicleDetailsModel)
      storeVehicleLookupFormModelInCache(model)
      Redirect(routes.Dispose.present)
    }.recoverWith {
      case e: Throwable => Future {
        Logger.debug(s"Web service call failed. Exception: ${e}")
        BadRequest("The remote server didn't like the request.")
      }
    }
  }

}


