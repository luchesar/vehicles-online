package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import mappings.disposal_of_vehicle.SetupTradeDetails._
import mappings.Postcode._
import play.api.Logger
import play.api.Play.current

object SetUpTradeDetails extends Controller {

  val traderLookupForm = Form(
    mapping(
      dealerNameId -> nonEmptyText(minLength = 2, maxLength = 100),
      dealerPostcodeId -> postcode()
    )(SetupTradeDetailsModel.apply)(SetupTradeDetailsModel.unapply)
  )

  def present = Action {
    implicit request =>
      Ok(views.html.disposal_of_vehicle.setup_trade_details(traderLookupForm))
  }

  def submit = Action {
    implicit request => {
      traderLookupForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.disposal_of_vehicle.setup_trade_details(formWithErrors)),
        f => {
          val key = mappings.disposal_of_vehicle.SetupTradeDetails.dealerNameId
          play.api.cache.Cache.set(key, f.traderBusinessName)
          Logger.debug(s"SetUpTradeDetails stored data in cache: key = $key, value = ${f.traderBusinessName}")
          Redirect(routes.BusinessChooseYourAddress.present)
        }
      )
    }
  }
}