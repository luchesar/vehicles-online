package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.disposal_of_vehicle.BusinessChooseYourAddressModel

object BusinessChooseYourAddress extends Controller {

  val businesssChooseYourAddressForm = Form(
    mapping(
      app.DisposalOfVehicle.businessNameID -> nonEmptyText(minLength = 1, maxLength = sixty)
    )(BusinessChooseYourAddressModel.apply)(BusinessChooseYourAddressModel.unapply)
  )

  def present = Action {
    implicit request =>
      Ok("Hello world") //Ok(views.html.disposal_of_vehicle.vehicle_lookup(vehicleLookupForm))
  }

  def submit = Action {
    implicit request => {
      businesssChooseYourAddressForm.bindFromRequest.fold(
        formWithErrors => BadRequest("Hello") , //BadRequest(views.html.disposal_of_vehicle.vehicle_lookup(formWithErrors)),
        f => Redirect(routes.VehicleLookup.present) //TODO: This needs to look at the correct next page
      )
    }
  }
}