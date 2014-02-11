package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.disposal_of_vehicle.BusinessChooseYourAddressModel
import mappings.disposal_of_vehicle.BusinessAddressSelect._
import modules._
import mappings.DropDown._

object BusinessChooseYourAddress extends Controller {
  val dropDownOptions = {
    val addressLookupService = injector.getInstance(classOf[services.AddressLookupService])
    addressLookupService.invoke("TEST") // TODO pass in postcode submitted on the previous page.
  }

  val businessChooseYourAddressForm = Form(
    mapping(
      businessNameId -> nonEmptyText(minLength = 1, maxLength = sixty),
      addressSelectId -> dropDown(dropDownOptions)
    )(BusinessChooseYourAddressModel.apply)(BusinessChooseYourAddressModel.unapply)
  )

  def present = Action {
    implicit request =>
      Ok(views.html.disposal_of_vehicle.business_choose_your_address(businessChooseYourAddressForm, dropDownOptions))
  }

  def submit = Action {
    implicit request => {
      businessChooseYourAddressForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.disposal_of_vehicle.business_choose_your_address(formWithErrors, dropDownOptions)),
        f => Redirect(routes.VehicleLookup.present)
      )
    }
  }
}