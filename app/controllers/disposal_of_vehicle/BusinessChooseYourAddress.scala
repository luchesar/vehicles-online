package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import models.domain.disposal_of_vehicle.{SetupTradeDetailsModel, TraderDetailsModel, BusinessChooseYourAddressModel}
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import mappings.common.DropDown
import DropDown._
import javax.inject.Inject
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global
import services.address_lookup.AddressLookupService
import common.{ClientSideSession, ClientSideSessionFactory, CookieImplicits}
import utils.helpers.FormExtensions._
import mappings.disposal_of_vehicle.EnterAddressManually._
import CookieImplicits.RequestCookiesAdapter
import CookieImplicits.SimpleResultAdapter
import CookieImplicits.FormAdapter
import mappings.common.Languages._
import play.api.data.FormError
import scala.Some
import play.api.Play.current
import play.api.i18n.Lang

final class BusinessChooseYourAddress @Inject()(addressLookupService: AddressLookupService)(implicit clientSideSessionFactory: ClientSideSessionFactory) extends Controller {

  private[disposal_of_vehicle] val form = Form(
    mapping(
      /* We cannot apply constraints to this drop down as it is populated by web call to an address lookup service.
      We would need the request here to get the cookie.
      Validation is done when we make a second web call with the UPRN, so if a bad guy is injecting a non-existent UPRN then it will fail at that step instead */
      AddressSelectId -> addressDropDown
    )(BusinessChooseYourAddressModel.apply)(BusinessChooseYourAddressModel.unapply)
  )

  def present = Action.async {
    implicit request =>
      request.cookies.getModel[SetupTradeDetailsModel] match {
        case Some(setupTradeDetailsModel) =>
          val session = clientSideSessionFactory.getSession(request.cookies)
          fetchAddresses(setupTradeDetailsModel)(session, lang).map {
            addresses =>
              Ok(views.html.disposal_of_vehicle.business_choose_your_address(form.fill(),
                setupTradeDetailsModel.traderBusinessName,
                setupTradeDetailsModel.traderPostcode,
                addresses))
          }
        case None => Future {
          Redirect(routes.SetUpTradeDetails.present())
        }
      }
  }

  def submit = Action.async { implicit request =>
    form.bindFromRequest.fold(
      formWithErrors =>
        request.cookies.getModel[SetupTradeDetailsModel] match {
          case Some(setupTradeDetailsModel) =>
            val session = clientSideSessionFactory.getSession(request.cookies)
            fetchAddresses(setupTradeDetailsModel)(session, lang).map {addresses =>
              val formWithReplacedErrors = formWithErrors.
                replaceError(AddressSelectId, "error.required", FormError(key = AddressSelectId, message = "disposal_businessChooseYourAddress.address.required", args = Seq.empty)).
                distinctErrors

              BadRequest(views.html.disposal_of_vehicle.business_choose_your_address(formWithReplacedErrors, setupTradeDetailsModel.traderBusinessName, setupTradeDetailsModel.traderPostcode, addresses))
          }
          case None => Future {
            Logger.error("Failed to find dealer details, redirecting")
            Redirect(routes.SetUpTradeDetails.present())
          }
        },
      f =>
        request.cookies.getModel[SetupTradeDetailsModel] match {
          case Some(setupTradeDetailsModel) =>
            implicit val session = clientSideSessionFactory.getSession(request.cookies)
            lookupUprn(f, setupTradeDetailsModel.traderBusinessName)
          case None => Future {
            Logger.error("Failed to find dealer details, redirecting")
            Redirect(routes.SetUpTradeDetails.present())
          }
        }
    )
  }

  def withLanguageCy = Action { implicit request =>
    Redirect(routes.BusinessChooseYourAddress.present()).
      withLang(langCy)
  }

  def withLanguageEn = Action { implicit request =>
    Redirect(routes.BusinessChooseYourAddress.present()).
      withLang(langEn)
  }

  private def fetchAddresses(setupTradeDetailsModel: SetupTradeDetailsModel)
                            (implicit session: ClientSideSession, lang: Lang) = {
    val postcode = setupTradeDetailsModel.traderPostcode
    addressLookupService.fetchAddressesForPostcode(postcode)
  }

  private def lookupUprn(model: BusinessChooseYourAddressModel, traderName: String)
                        (implicit request: Request[_], session: ClientSideSession) = {
    val lookedUpAddress = addressLookupService.fetchAddressForUprn(model.uprnSelected.toString)
    lookedUpAddress.map {
      case Some(addressViewModel) =>
        val traderDetailsModel = TraderDetailsModel(traderName = traderName, traderAddress = addressViewModel)
        /* The redirect is done as the final step within the map so that:
         1) we are not blocking threads
         2) the browser does not change page before the future has completed and written to the cache.
         */
        Redirect(routes.VehicleLookup.present()).
          discardingCookie(EnterAddressManuallyCacheKey).
          withCookie(model).
          withCookie(traderDetailsModel)
      case None => Redirect(routes.UprnNotFound.present())
    }
  }
}
