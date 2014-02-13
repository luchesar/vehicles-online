package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import play.api.Play.current
import models.domain.disposal_of_vehicle.{VehicleDetailsModel, DealerDetailsModel, VehicleLookupFormModel}
import mappings.disposal_of_vehicle.VehicleLookup._
import mappings.V5cReferenceNumber._
import mappings.V5cRegistrationNumber._
import mappings.Postcode._
import controllers.disposal_of_vehicle.Helpers._
import scala.Some
import models.domain.common.Address

object VehicleLookup extends Controller {

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
    {
      fetchDealerDetailsFromCache match {
        case Some(dealerDetails) => Ok(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, vehicleLookupForm))
        case None => Redirect(routes.SetUpTradeDetails.present) // TODO write controller and integration tests for re-routing when not logged in.
      }
    }
  }

  def submit = Action {
    implicit request => {
      vehicleLookupForm.bindFromRequest.fold(
        formWithErrors => {
          fetchDealerDetailsFromCache match {
            case Some(dealerDetails) => BadRequest(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, formWithErrors))
            case None => Redirect(routes.SetUpTradeDetails.present) // TODO write controller and integration tests for re-routing when not logged in.
          }
        },
        f => {
          saveVehicleDetailsToCache(lookupVehicleDetails(f))
          Redirect(routes.Dispose.present)
        }
      )
    }
  }

  private def lookupVehicleDetails(f: VehicleLookupFormModel) = {
    Logger.debug(s"Looking up vehicle details for ${v5cReferenceNumberId}: ${f.v5cReferenceNumber}, ${v5cRegistrationNumberId}: ${f.v5cRegistrationNumber}, ${v5cKeeperNameId}: ${f.v5cKeeperName}, ${v5cPostcodeId}: ${f.v5cPostcode}")

    val knownReferenceNumber = "11111111111"
    if (f.v5cReferenceNumber == knownReferenceNumber) {
      Logger.debug(s"Selecting vehicle for ref number ${knownReferenceNumber}")
      VehicleDetailsModel(vehicleMake = "Alfa Romeo",
        vehicleModel = "Alfasud ti",
        keeperName = f.v5cKeeperName,
        keeperAddress = Address("1 The Avenue", Some("Earley"), Some("Reading"), None, f.v5cPostcode))
    } else {
      Logger.debug("Selecting default vehicle")
      VehicleDetailsModel(vehicleMake = "PEUGEOT",
        vehicleModel = "307 CC",
        keeperName = f.v5cKeeperName,
        keeperAddress = Address("1 The Avenue", Some("Earley"), Some("Reading"), None, f.v5cPostcode))
    }
  }

  private def saveVehicleDetailsToCache(vehicleDetailsModel: VehicleDetailsModel) = {
    val key = mappings.disposal_of_vehicle.VehicleLookup.cacheKey
    val value = vehicleDetailsModel
    play.api.cache.Cache.set(key, value)
    Logger.debug(s"VehicleLookup page - stored vehicle details object in cache: key = $key, value = ${value}")
  }

}


