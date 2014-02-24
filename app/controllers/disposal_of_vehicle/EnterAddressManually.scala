package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import mappings.common.AddressAndPostcode._
import mappings.disposal_of_vehicle.DealerDetails
import mappings.common.AddressAndPostcode
import controllers.disposal_of_vehicle.Helpers._
import models.domain.disposal_of_vehicle.{AddressViewModel, DealerDetailsModel, EnterAddressManuallyModel}
import play.api.Logger
import play.api.Play.current

object EnterAddressManually extends Controller {
  val form = Form(
    mapping(
      AddressAndPostcode.id -> addressAndPostcode
    )(EnterAddressManuallyModel.apply)(EnterAddressManuallyModel.unapply)
  )

  def present = Action {
    implicit request =>
      fetchDealerNameFromCache match {
        case Some(name) => Ok(views.html.disposal_of_vehicle.enter_address_manually(form))
        case None => Redirect(routes.SetUpTradeDetails.present)
      }
  }

  def submit = Action {
    implicit request => {
      form.bindFromRequest.fold(
        formWithErrors =>
          fetchDealerNameFromCache match {
            case Some(name) => BadRequest(views.html.disposal_of_vehicle.enter_address_manually(formWithErrors))
            case None => {
              Logger.error("failed to find dealer name in cache for formWithErrors, redirecting...")
              Redirect(routes.SetUpTradeDetails.present)
            }
          },
        f =>
          fetchDealerNameFromCache match {
          case Some(name) => {
            storeDealerDetailsInCache(f, name)
            Redirect(routes.VehicleLookup.present)
          }
          case None => {
            Logger.error("failed to find dealer name in cache on submit, redirecting...")
            Redirect(routes.SetUpTradeDetails.present)
          }
        }
      )
    }
  }

  def storeDealerDetailsInCache(model: EnterAddressManuallyModel, dealerName: String) = { // TODO possible code re-use with BusinessChooseYourAddress if this was extracted to a helper.
    val dealerAddress = AddressViewModel.from(model.addressAndPostcodeModel)
    val key = DealerDetails.cacheKey
    val value = DealerDetailsModel(dealerName = dealerName, dealerAddress = dealerAddress)
    play.api.cache.Cache.set(key, value)
    Logger.debug(s"EnterAddressManually stored data in cache: key = $key, value = ${value}")
  }
}