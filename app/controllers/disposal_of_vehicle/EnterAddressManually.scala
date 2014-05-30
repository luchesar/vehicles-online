package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.Logger
import mappings.common.AddressAndPostcode._
import models.domain.disposal_of_vehicle.{TraderDetailsModel, AddressViewModel, SetupTradeDetailsModel, EnterAddressManuallyModel}
import utils.helpers.FormExtensions._
import com.google.inject.Inject
import common.{ClientSideSessionFactory, CookieImplicits}
import CookieImplicits.RequestCookiesAdapter
import CookieImplicits.SimpleResultAdapter
import CookieImplicits.FormAdapter

final class EnterAddressManually @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  val form = Form(
    mapping(
      AddressAndPostcodeId -> addressAndPostcode
    )(EnterAddressManuallyModel.apply)(EnterAddressManuallyModel.unapply)
  )

  def present = Action {
    implicit request =>
      request.cookies.getModel[SetupTradeDetailsModel] match {
        case Some(_) =>
          Ok(views.html.disposal_of_vehicle.enter_address_manually(form.fill()))
        case None => Redirect(routes.SetUpTradeDetails.present())
      }
  }

  def submit = Action {
    implicit request => {
      form.bindFromRequest.fold(
        formWithErrors =>
          request.cookies.getModel[SetupTradeDetailsModel] match {
            case Some(_) =>
              val updatedFormWithErrors = formWithErrors
                .replaceError("addressAndPostcode.addressLines.line1", "error.required", FormError("addressAndPostcode.addressLines", "error.address.line1Required"))
                .replaceError("addressAndPostcode.addressLines.line1", "error.minLength", FormError("addressAndPostcode.addressLines", "error.address.line1minLength"))
                .replaceError("addressAndPostcode.addressLines.line4", "error.required", FormError("addressAndPostcode.addressLines", "error.address.line4Required"))
                .replaceError("addressAndPostcode.addressLines.line4", "error.minLength", FormError("addressAndPostcode.addressLines", "error.address.line4minLength"))
              BadRequest(views.html.disposal_of_vehicle.enter_address_manually(updatedFormWithErrors))
            case None =>
              Logger.debug("failed to find dealer name in cache for formWithErrors, redirecting...")
              Redirect(routes.SetUpTradeDetails.present())
          },
        f =>
          request.cookies.getModel[SetupTradeDetailsModel].map(_.traderBusinessName) match {
          case Some(traderBusinessName) =>
            val traderAddress = AddressViewModel.from(f.stripCharsNotAccepted.addressAndPostcodeModel)
            val traderDetailsModel = TraderDetailsModel(traderName = traderBusinessName, traderAddress = traderAddress)

            Redirect(routes.VehicleLookup.present()).
              withCookie(f).
              withCookie(traderDetailsModel)
          case None =>
            Logger.debug("failed to find dealer name in cache on submit, redirecting...")
            Redirect(routes.SetUpTradeDetails.present())
          }
      )
    }
  }
}

