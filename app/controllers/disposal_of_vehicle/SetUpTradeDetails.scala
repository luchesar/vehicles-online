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
      dealerNameId -> traderBusinessName(minLength = dealerNameMinLength, maxLength = dealerNameMaxLength),
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
          replaceError("dealerName", "error.minLength", FormError("dealerName", "error.validTraderBusinessName")).
          replaceError("dealerName", "error.required", FormError("dealerName", "error.validTraderBusinessName"))
        // TODO we don't want 3 of the same error message so remore duplicates from the list. This means a 'distinctErrors' func on the FormExtensions
        BadRequest(views.html.disposal_of_vehicle.setup_trade_details(formWithReplacedErrors))
      },
      f => {
        storeTradeDetailsInCache(f)
        Redirect(routes.BusinessChooseYourAddress.present)
      }
    )
  }
}