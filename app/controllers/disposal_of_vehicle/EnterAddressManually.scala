package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.{FormError, Form}
import play.api.data.Forms._
import play.api.Logger
import mappings.common.AddressAndPostcode._
import models.domain.disposal_of_vehicle.{TraderDetailsModel, AddressViewModel, SetupTradeDetailsModel, EnterAddressManuallyModel}
import utils.helpers.FormExtensions._
import com.google.inject.Inject
import common.EncryptedCookieImplicits
import EncryptedCookieImplicits.RequestAdapter
import EncryptedCookieImplicits.SimpleResultAdapter
import EncryptedCookieImplicits.FormAdapter
import utils.helpers.{CookieNameHashing, CookieEncryption}
import common.EncryptedCookieImplicits

class EnterAddressManually @Inject()()(implicit encryption: CookieEncryption, hashing: CookieNameHashing) extends Controller {

  val form = Form(
    mapping(
      addressAndPostcodeId -> addressAndPostcode
    )(EnterAddressManuallyModel.apply)(EnterAddressManuallyModel.unapply)
  )

  def present = Action {
    implicit request =>
      request.getEncryptedCookie[SetupTradeDetailsModel] match {
        case Some(_) => Ok(views.html.disposal_of_vehicle.enter_address_manually(form.fill()))
        case None => Redirect(routes.SetUpTradeDetails.present())
      }
  }

  def submit = Action {
    implicit request => {
      form.bindFromRequest.fold(
        formWithErrors =>
          request.getEncryptedCookie[SetupTradeDetailsModel] match {
            case Some(_) =>
              val updatedFormWithErrors = formWithErrors.replaceError("addressAndPostcode.addressLines.line1", "error.required", FormError("addressAndPostcode.addressLines", "error.address.line1Required"))
              BadRequest(views.html.disposal_of_vehicle.enter_address_manually(updatedFormWithErrors))
            case None =>
              Logger.debug("failed to find dealer name in cache for formWithErrors, redirecting...")
              Redirect(routes.SetUpTradeDetails.present())
          },
        f =>
          request.getEncryptedCookie[SetupTradeDetailsModel].map(_.traderBusinessName) match {
          case Some(name) =>
            val dealerAddress = AddressViewModel.from(f.stripCharsNotAccepted.addressAndPostcodeModel)
            val dealerDetailsModel = TraderDetailsModel(traderName = name, traderAddress = dealerAddress)

            Redirect(routes.VehicleLookup.present()).withEncryptedCookie(dealerDetailsModel)
          case None =>
            Logger.debug("failed to find dealer name in cache on submit, redirecting...")
            Redirect(routes.SetUpTradeDetails.present())
          }
      )
    }
  }
}

