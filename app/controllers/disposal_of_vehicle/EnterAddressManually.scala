package controllers.disposal_of_vehicle

import com.google.inject.Inject
import common.ClientSideSessionFactory
import common.CookieImplicits.FormAdapter
import common.CookieImplicits.RequestCookiesAdapter
import common.CookieImplicits.SimpleResultAdapter
import mappings.common.AddressAndPostcode._
import mappings.common.Languages._
import models.domain.disposal_of_vehicle.{TraderDetailsModel, AddressViewModel, SetupTradeDetailsModel, EnterAddressManuallyModel}
import play.api.Logger
import play.api.Play.current
import play.api.data.Form
import play.api.data.FormError
import play.api.data.Forms._
import play.api.mvc._
import utils.helpers.FormExtensions._
import models.domain.common.{AddressLinesModel, AddressAndPostcodeModel}

final class EnterAddressManually @Inject()()(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  private[disposal_of_vehicle] val form = Form(
    mapping(
      AddressAndPostcodeId -> addressAndPostcode
    )(EnterAddressManuallyModel.apply)(EnterAddressManuallyModel.unapply)
  )

  def present = Action {
    implicit request =>
      request.cookies.getModel[SetupTradeDetailsModel] match {
        case Some(setupTradeDetailsModel) =>
          Ok(views.html.disposal_of_vehicle.enter_address_manually(form.fill(), setupTradeDetailsModel.traderPostcode))
        case None => Redirect(routes.SetUpTradeDetails.present())
      }
  }

  def submit = Action {
    implicit request => {
      form.bindFromRequest.fold(
        formWithErrors =>
          request.cookies.getModel[SetupTradeDetailsModel] match {
            case Some(setupTradeDetailsModel) =>
              val updatedFormWithErrors = formWithErrors.
                replaceError("addressAndPostcode.addressLines.buildingNameOrNumber", FormError("addressAndPostcode.addressLines", "error.address.buildingNameOrNumber.invalid")).
                replaceError("addressAndPostcode.addressLines.postTown", FormError("addressAndPostcode.addressLines", "error.address.postTown")).
                replaceError("addressAndPostcode.postcode", FormError("addressAndPostcode.postcode", "error.address.postcode.invalid")).
                distinctErrors
              BadRequest(views.html.disposal_of_vehicle.enter_address_manually(updatedFormWithErrors, setupTradeDetailsModel.traderPostcode))
            case None =>
              Logger.debug("Failed to find dealer name in cache, redirecting")
              Redirect(routes.SetUpTradeDetails.present())
          },
        f =>
          request.cookies.getModel[SetupTradeDetailsModel].map(_.traderBusinessName) match {
            case Some(traderBusinessName) =>
              val updatedForm: EnterAddressManuallyModel = f.stripCharsNotAccepted.toUpperCase
              val traderAddress = AddressViewModel.from(updatedForm.addressAndPostcodeModel)
              val traderDetailsModel = TraderDetailsModel(traderName = traderBusinessName, traderAddress = traderAddress)

              Redirect(routes.VehicleLookup.present()).
                withCookie(updatedForm).
                withCookie(traderDetailsModel)
            case None =>
              Logger.debug("Failed to find dealer name in cache on submit, redirecting")
              Redirect(routes.SetUpTradeDetails.present())
          }
      )
    }
  }

  def withLanguageCy = Action { implicit request =>
    Redirect(routes.EnterAddressManually.present()).
      withLang(langCy)
  }

  def withLanguageEn = Action { implicit request =>
    Redirect(routes.EnterAddressManually.present()).
      withLang(langEn)
  }
}

