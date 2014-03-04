package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import mappings.disposal_of_vehicle.SetupTradeDetails._
import play.api.Logger
import play.api.Play.current
import mappings.common.Postcode._
import controllers.disposal_of_vehicle.Helpers.storeTradeDetailsInCache

object SetUpTradeDetails extends Controller {

  val traderLookupForm = Form(
    mapping(
      dealerNameId -> nonEmptyText(minLength = 2, maxLength = 100),
      dealerPostcodeId -> postcode()
    )(SetupTradeDetailsModel.apply)(SetupTradeDetailsModel.unapply)
  )

  def present = Action { implicit request =>
//    fetchModelFromSession
    Ok(views.html.disposal_of_vehicle.setup_trade_details(traderLookupForm))
  }

  def submit = Action {
    implicit request => {
      traderLookupForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.disposal_of_vehicle.setup_trade_details(formWithErrors)),
        f => {
          storeTradeDetailsInCache(f)
          Redirect(routes.BusinessChooseYourAddress.present)
        }
      )
    }
  }
}