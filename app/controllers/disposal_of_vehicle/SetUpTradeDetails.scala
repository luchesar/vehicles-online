package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import mappings.disposal_of_vehicle.SetupTradeDetails._
import mappings.common.Postcode._
import controllers.disposal_of_vehicle.Helpers.storeTradeDetailsInCache
import utils.helpers.FormExtensions._

object SetUpTradeDetails extends Controller {

  val traderLookupForm = Form(
    mapping(
      dealerNameId -> traderBusinessName(),
      dealerPostcodeId -> postcode
    )(SetupTradeDetailsModel.apply)(SetupTradeDetailsModel.unapply)
  )

  def present = Action { implicit request =>
    Ok(views.html.disposal_of_vehicle.setup_trade_details(traderLookupForm))
  }

  def submit = Action { implicit request =>
    traderLookupForm.bindFromRequest.fold(
      formWithErrors => {
        val formWithReplacedErrors = formWithErrors.
          replaceError(dealerNameId, FormError(key = dealerNameId, message = "error.validTraderBusinessName", args = Seq.empty)).
          replaceError(dealerPostcodeId, FormError(key = dealerPostcodeId, message = "error.restricted.validPostcode", args = Seq.empty)).
          distinctErrors
        BadRequest(views.html.disposal_of_vehicle.setup_trade_details(formWithReplacedErrors))
      },
      f => {
        storeTradeDetailsInCache(f)
        Redirect(routes.BusinessChooseYourAddress.present)
      }
    )
  }
}