package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import mappings.common.AddressAndPostcode._
import controllers.disposal_of_vehicle.Helpers._
import models.domain.disposal_of_vehicle.EnterAddressManuallyModel

object EnterAddressManually extends Controller {
  val form = Form(
    mapping(
      id -> addressAndPostcode
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
}