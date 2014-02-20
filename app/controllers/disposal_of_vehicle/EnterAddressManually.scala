package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.{AddressAndPostcodeModel, EnterAddressManuallyModel}
import mappings.disposal_of_vehicle.AddressAndPostcode._
import models.domain.disposal_of_vehicle.AddressAndPostcodeModel
import mappings.disposal_of_vehicle.AddressAndPostcode

object EnterAddressManually extends Controller {
  val form = Form(
    mapping(
      AddressAndPostcode.id -> addressAndPostcode
    )(EnterAddressManuallyModel.apply)(EnterAddressManuallyModel.unapply)
  )

  def present = Action {
    implicit request =>
      Ok(views.html.disposal_of_vehicle.enter_address_manually(form))
  }

  def submit = Action {
    implicit request => {
      form.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.disposal_of_vehicle.enter_address_manually(formWithErrors)),
        f =>  Redirect(routes.VehicleLookup.present)

      )
    }
  }
}