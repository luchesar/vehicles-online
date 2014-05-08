package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import models.domain.disposal_of_vehicle.{SetupTradeDetailsModel, DealerDetailsModel, BusinessChooseYourAddressModel}
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import mappings.common.DropDown
import DropDown._
import javax.inject.Inject
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global
import services.address_lookup.AddressLookupService
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState.RequestAdapter
import controllers.disposal_of_vehicle.DisposalOfVehicleSessionState.SimpleResultAdapter

class BusinessChooseYourAddress @Inject()(addressLookupService: AddressLookupService) extends Controller {

  private def fetchAddresses(setupTradeDetailsModel: SetupTradeDetailsModel) = {
    val postcode = setupTradeDetailsModel.traderPostcode
    addressLookupService.fetchAddressesForPostcode(postcode)
  }

  val form = Form(
    mapping(
      /* We cannot apply constraints to this drop down as it is populated by web call to an address lookup service.
      Validation is done when we make a second web call with the UPRN, so if a bad guy is injecting a non-existent UPRN then it will fail at that step instead */
      addressSelectId -> dropDown
    )(BusinessChooseYourAddressModel.apply)(BusinessChooseYourAddressModel.unapply)
  )

  def present = Action.async {
    implicit request =>
      request.fetch[SetupTradeDetailsModel] match {
        case Some(dealerDetails) =>
          fetchAddresses(dealerDetails).map {
            addresses =>
              val f = request.fetch[BusinessChooseYourAddressModel] match {
                case Some(cached) => form.fill(cached)
                case None => form // Blank form.
              }
              Ok(views.html.disposal_of_vehicle.business_choose_your_address(f, dealerDetails.traderBusinessName, addresses))
          }
        case None => Future {
          Redirect(routes.SetUpTradeDetails.present)
        }
      }
  }

  def submit = Action.async { implicit request =>
      form.bindFromRequest.fold(
        formWithErrors =>
          request.fetch[SetupTradeDetailsModel] match {
            case Some(dealerDetails) => fetchAddresses(dealerDetails).map {
              addresses => BadRequest(views.html.disposal_of_vehicle.business_choose_your_address(formWithErrors, dealerDetails.traderBusinessName, addresses))
            }
            case None => Future {
              Logger.error("Failed to find dealer details in cache for submit formWithErrors, redirecting...")
              Redirect(routes.SetUpTradeDetails.present)
            }
          },
        f =>
          request.fetch[SetupTradeDetailsModel] match {
            case Some(dealerDetails) =>
              lookupUprn(f, dealerDetails.traderBusinessName)
            case None => Future {
              Logger.error("Failed to find dealer details in cache on submit valid form, redirecting...")
              Redirect(routes.SetUpTradeDetails.present)
            }
          }
      )
  }

  private def lookupUprn(model: BusinessChooseYourAddressModel, dealerName: String) = {
    val lookedUpAddress = addressLookupService.fetchAddressForUprn(model.uprnSelected.toString)
    lookedUpAddress.map {
      case Some(addr) => {
        val dealerDetailsModel = DealerDetailsModel(dealerName = dealerName, dealerAddress = addr) // TODO is this redundant??? Delete and test.
        /* The redirect is done as the final step within the map so that:
         1) we are not blocking threads
         2) the browser does not change page before the future has completed and written to the cache.
         */
        Redirect(routes.VehicleLookup.present).withCookie(model).withCookie(dealerDetailsModel)
      }
      case None => Redirect(routes.UprnNotFound.present)
    }
  }
}