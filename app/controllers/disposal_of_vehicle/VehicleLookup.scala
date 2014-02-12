package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.{DealerDetailsModel, VehicleLookupFormModel}
import mappings.disposal_of_vehicle.VehicleLookup._
import mappings.V5cReferenceNumber._
import mappings.V5cRegistrationNumber._
import mappings.Postcode._
import controllers.disposal_of_vehicle.Helpers._
import models.domain.disposal_of_vehicle.DealerDetailsModel
import models.domain.disposal_of_vehicle.VehicleLookupFormModel
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
      retrieveTraderBusinessName match {
        case Some(traderBusinessName) => Ok(views.html.disposal_of_vehicle.vehicle_lookup(fetchData, vehicleLookupForm))
        case None => Redirect(routes.SetUpTradeDetails.present) // TODO write controller and integration tests for re-routing when not logged in.
      }
    }
  }

  def submit = Action {
    implicit request => {
      vehicleLookupForm.bindFromRequest.fold(
        formWithErrors => {
          retrieveTraderBusinessName match {
            case Some(traderBusinessName) => BadRequest(views.html.disposal_of_vehicle.vehicle_lookup(fetchData, formWithErrors))
            case None => Redirect(routes.SetUpTradeDetails.present) // TODO write controller and integration tests for re-routing when not logged in.
          }
        },
        f => Redirect(routes.Dispose.present)
      )
    }
  }

  private def fetchData: DealerDetailsModel  = {
    DealerDetailsModel(dealerName = "Dealer name",
      dealerAddress = Address("Address line 1", Some("Address line 2"), Some("Address line 3"), None, "Postcode"))
  }
}


