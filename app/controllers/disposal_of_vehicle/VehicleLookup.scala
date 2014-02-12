package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.VehicleLookupFormModel
import mappings.disposal_of_vehicle.VehicleLookup._
import mappings.V5cReferenceNumber._
import mappings.V5cRegistrationNumber._
import mappings.Postcode._

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
      Ok(views.html.disposal_of_vehicle.vehicle_lookup(vehicleLookupForm))
  }

  def submit = Action {
    implicit request => {
      vehicleLookupForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.disposal_of_vehicle.vehicle_lookup(formWithErrors)),
        f => Redirect(routes.Dispose.present)
      )
    }
  }

//  private def fetchData: DisposeModel  = {
//    Model(vehicleMake = "PEUGEOT",
//      vehicleModel = "307 CC",
//      keeperName = "Mrs Anne Shaw",
//      keeperAddress = Address("1 The Avenue", Some("Earley"), Some("Reading"), None, "RG12 6HT"),
//      dealerName = "Car Giant",
//      dealerAddress = Address("44 Hythe Road", Some("White City"), Some("London"), None, "NW10 6RJ"))
//  }
}