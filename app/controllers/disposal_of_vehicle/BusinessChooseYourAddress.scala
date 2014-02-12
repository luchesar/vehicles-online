package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.BusinessChooseYourAddressModel
import mappings.disposal_of_vehicle.BusinessAddressSelect._
import modules._
import mappings.DropDown._
import controllers.disposal_of_vehicle.Helpers._

object BusinessChooseYourAddress extends Controller {
  val dropDownOptions = {
    val addressLookupService = injector.getInstance(classOf[services.AddressLookupService])
    addressLookupService.invoke("TEST") // TODO pass in postcode submitted on the previous page.
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
        case None => Redirect(routes.SetUpTradeDetails.present) // TODO write controller and integration tests for re-routing when not logged in.
      }
    }
  }

  def submit = Action {
    implicit request => {
      businessChooseYourAddressForm.bindFromRequest.fold(
        formWithErrors => {
          retrieveTraderBusinessName match {
            case Some(traderBusinessName) => BadRequest(views.html.disposal_of_vehicle.business_choose_your_address(formWithErrors, traderBusinessName, dropDownOptions))
            case None => Redirect(routes.SetUpTradeDetails.present) // TODO write controller and integration tests for re-routing when not logged in.
          }
        },
        f => Redirect(routes.VehicleLookup.present)
      )
    }
  }
}