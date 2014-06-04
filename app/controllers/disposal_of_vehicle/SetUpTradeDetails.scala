package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.SetupTradeDetailsModel
import mappings.disposal_of_vehicle.SetupTradeDetails._
import common.{ClientSideSessionFactory, CookieImplicits}
import CookieImplicits.SimpleResultAdapter
import mappings.common.Postcode._
import utils.helpers.FormExtensions._
import com.google.inject.Inject
import CookieImplicits.FormAdapter
import utils.helpers.CookieNameHashing

final class SetUpTradeDetails @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  val form = Form(
    mapping(
      TraderNameId -> traderBusinessName(),
      TraderPostcodeId -> postcode
    )(SetupTradeDetailsModel.apply)(SetupTradeDetailsModel.unapply)
  )

  def present = Action {
    implicit request =>
      Ok(views.html.disposal_of_vehicle.setup_trade_details(form.fill()))
  }

  def submit = Action {
    implicit request =>
      form.bindFromRequest.fold(
        formWithErrors => {
          val formWithReplacedErrors = formWithErrors.
            replaceError(TraderNameId, FormError(key = TraderNameId, message = "error.validTraderBusinessName", args = Seq.empty)).
            replaceError(TraderPostcodeId, FormError(key = TraderPostcodeId, message = "error.restricted.validPostcode", args = Seq.empty)).
            distinctErrors
          BadRequest(views.html.disposal_of_vehicle.setup_trade_details(formWithReplacedErrors))
        },
        f => Redirect(routes.BusinessChooseYourAddress.present()).withCookie(f)
      )
  }
}
