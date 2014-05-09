package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import mappings.disposal_of_vehicle.SetupTradeDetails._
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState.SimpleResultAdapter
import mappings.common.Postcode._
import utils.helpers.FormExtensions._
import com.google.inject.Inject
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState.RequestAdapter
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState.FormAdapter

class SetUpTradeDetails @Inject()() extends Controller {

  val traderLookupForm = Form(
    mapping(
      traderNameId -> traderBusinessName(),
      traderPostcodeId -> postcode
    )(SetupTradeDetailsModel.apply)(SetupTradeDetailsModel.unapply)
  )

  def present = Action {
    implicit request =>
      Ok(views.html.disposal_of_vehicle.setup_trade_details(traderLookupForm.fill(request.getCookie[SetupTradeDetailsModel])))
  }

  def submit = Action {
    implicit request =>
      traderLookupForm.bindFromRequest.fold(
        formWithErrors => {
          val formWithReplacedErrors = formWithErrors.
            replaceError(traderNameId, FormError(key = traderNameId, message = "error.validTraderBusinessName", args = Seq.empty)).
            replaceError(traderPostcodeId, FormError(key = traderPostcodeId, message = "error.restricted.validPostcode", args = Seq.empty)).
            distinctErrors
          BadRequest(views.html.disposal_of_vehicle.setup_trade_details(formWithReplacedErrors))
        },
        f => Redirect(routes.BusinessChooseYourAddress.present).withCookie(f)
      )
  }
}