package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.BusinessChooseYourAddressModel
import mappings.disposal_of_vehicle.BusinessAddressSelect._
import modules._
import mappings.DropDown._
import controllers.disposal_of_vehicle.Helpers._
import play.api.Logger
import play.api.Play.current

object BusinessChooseYourAddress extends Controller {
  val addressLookupService = injector.getInstance(classOf[services.AddressLookupService])

  def dropDownOptions = {
    addressLookupService.fetchAddress("TEST") // TODO pass in postcode submitted on the previous page.
  }

  val businessChooseYourAddressForm = Form(
    mapping(
      addressSelectId -> dropDown(dropDownOptions)
    )(BusinessChooseYourAddressModel.apply)(BusinessChooseYourAddressModel.unapply)
  )

  def present = Action {
    implicit request =>
    {
      retrieveTraderBusinessName match {
        case Some(traderBusinessName) => Ok(views.html.disposal_of_vehicle.business_choose_your_address(businessChooseYourAddressForm, traderBusinessName, dropDownOptions))
        case None => Redirect(routes.SetUpTradeDetails.present)
      }
    }
  }

  def submit = Action {
    implicit request => {
      businessChooseYourAddressForm.bindFromRequest.fold(
        formWithErrors => {
          retrieveTraderBusinessName match {
            case Some(traderBusinessName) => BadRequest(views.html.disposal_of_vehicle.business_choose_your_address(formWithErrors, traderBusinessName, dropDownOptions))
            case None => Redirect(routes.SetUpTradeDetails.present)
          }
        },
        f => {
          val key = mappings.disposal_of_vehicle.BusinessAddressSelect.addressSelectId
          val value = addressLookupService.lookupAddress(f.addressSelected)
          play.api.cache.Cache.set(key, value)
          Logger.debug(s"BusinessChooseYourAddress stored data in cache: key = $key, value = ${value}")
          Redirect(routes.VehicleLookup.present)
        }
      )
    }
  }
}