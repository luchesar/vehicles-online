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

  private[disposal_of_vehicle] val form = Form(
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
              val updatedFormWithErrors = formWithErrors.
                replaceError("addressAndPostcode.addressLines.buildingNameOrNumber", FormError("addressAndPostcode.addressLines", "error.address.buildingNameOrNumber.invalid")).
                replaceError("addressAndPostcode.addressLines.postTown", FormError("addressAndPostcode.addressLines", "error.address.postTown")).
                replaceError("addressAndPostcode.postcode", FormError("addressAndPostcode.postcode", "error.address.postcode.invalid")).
                distinctErrors
              BadRequest(views.html.disposal_of_vehicle.enter_address_manually(updatedFormWithErrors))
            case None =>
              Logger.debug("Failed to find dealer name in cache, redirecting")
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
            Logger.debug("Failed to find dealer name in cache on submit, redirecting")
            Redirect(routes.SetUpTradeDetails.present())
          }
      )
    }
  }
}

