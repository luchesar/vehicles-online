package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
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
//          lookupVehicleDetails(f)
          Redirect(routes.Dispose.present)
        }
      )
    }
  }

//  private def lookupVehicleDetails(f: VehicleLookupFormModel) {
//    Logger.debug(s"Looking up vehicle details for v5cReferenceNumber: ${f.v5cReferenceNumber}, v5cRegistrationNumber: ${f.v5cRegistrationNumber}")
//    val vehicleDetailsModel(vehicleMake = "PEUGEOT",
//      vehicleModel = "307 CC",
//      keeperName = "Mrs Anne Shaw",
//      keeperAddress = Address("1 The Avenue", Some("Earley"), Some("Reading"), None, "RG12 6HT"))
//  }
}


