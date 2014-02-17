package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import mappings.common.MultiLineAddress
import mappings.common.PostCode
import mappings.common.MultiLineAddress._
import mappings.common.PostCode._
import constraints.MultilineAddress.requiredAddress
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.EnterAddressManuallyModel

object EnterAddressManually extends Controller {
  val form = Form(
    mapping(
      MultiLineAddress.id -> address.verifying(requiredAddress),
      PostCode.key -> postcode()
    )(EnterAddressManuallyModel.apply)(EnterAddressManuallyModel.unapply)
  )

  def present = Action {
    implicit request => Ok("hello") //Ok(views.html.disposal_of_vehicle.business_choose_your_address(form, name, fetchAddresses))
  }

  def submit = Action { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors => BadRequest("bad submit"), //BadRequest(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, formWithErrors)),
      f => Redirect(routes.VehicleLookup.present)
    )
  }
}