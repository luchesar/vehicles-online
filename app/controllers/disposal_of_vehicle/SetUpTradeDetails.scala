package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import controllers.Mappings._
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import app.DisposalOfVehicle.SetupTradeDetails._
import mappings.Postcode
import mappings.Postcode._

object SetUpTradeDetails extends Controller {

  val traderLookupForm = Form(
    mapping(
      traderBusinessNameID -> nonEmptyText(minLength = 2, maxLength = 100),
      traderPostcodeID -> postcode()
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
        f => Redirect(routes.BusinessChooseYourAddress.present)
      )
    }
  }
}