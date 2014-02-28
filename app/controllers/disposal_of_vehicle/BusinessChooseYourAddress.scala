package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.{DealerDetailsModel, BusinessChooseYourAddressModel}
import mappings.disposal_of_vehicle.BusinessChooseYourAddress._
import mappings.common.DropDown
import DropDown._
import controllers.disposal_of_vehicle.Helpers._
import play.api.Logger
import javax.inject.Inject
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global

class BusinessChooseYourAddress @Inject()(addressLookupService: services.AddressLookupService) extends Controller {
  private lazy val fetchAddresses = {
    /* Needs to be a lazy val otherwise when the page is IoC'd the form will execute it, so if you were jumping to the
     page with nothing in the cache it will blow up in the constructor before it gets to the code to redirect to another page.
     */
    val postcode = fetchTraderDetailsFromCache match {
      case Some(setupTradeDetailsModel) => setupTradeDetailsModel.traderPostcode
      case None => ??? //"TEST"
    }

    addressLookupService.fetchAddressesForPostcode(postcode)
  }

  val form = Form(
    mapping(
      /* We cannot apply constraints to this drop down as it is populated by web call to an address lookup service.
      Validation is done when we make a second web call with the UPRN, so if a bad guy is injecting a non-existent UPRN
      then it will fail at that step instead */
      addressSelectId -> dropDown
    )(BusinessChooseYourAddressModel.apply)(BusinessChooseYourAddressModel.unapply)
  )

  def present = Action.async {
    implicit request =>
      fetchTraderDetailsFromCache match {
        case Some(dealerDetails) => {
          fetchAddresses.map { addresses =>
            val f = fetchBusinessChooseYourAddressModelFromCache match {
              case Some(cached) => form.fill(cached)
              case None => form // Blank form.
            }
            Ok(views.html.disposal_of_vehicle.business_choose_your_address(f, dealerDetails.traderBusinessName, addresses))
          }
        }
        case None => Future {
          Redirect(routes.SetUpTradeDetails.present)
        }
      }
  }

  def submit = Action.async {
    implicit request => {
      /* TODO [SKW] I realised how we would handle redirect when something missing from cache in Carers, pass what we
      *  want to do next in as a function:
       * implicit request => {
       *    dependsOnTraderDetails {
       *      form.bindFromRequest.fold(
       *        ***
       *    )
       *  }
       * }
       *
       * def dependsOnTraderDetails (f: SimpleResult) = {
       *    if(is in cache) f //
       *    else {
       *      Logger.error("failed to find dealer name in cache for formWithErrors, redirecting...")
       *      Redirect(routes.SetUpTradeDetails.present)
       *    }
       * }
       */

      form.bindFromRequest.fold(
        formWithErrors =>
          fetchTraderDetailsFromCache match {
            case Some(dealerDetails) => fetchAddresses.map { addresses =>
              BadRequest(views.html.disposal_of_vehicle.business_choose_your_address(formWithErrors, dealerDetails.traderBusinessName, addresses))
            }
            case None => Future {
              Logger.error("failed to find dealer name in cache for formWithErrors, redirecting...")
              Redirect(routes.SetUpTradeDetails.present)
            }
          },
        f =>
          fetchTraderDetailsFromCache match {
            case Some(dealerDetails) => {
              storeBusinessChooseYourAddressModelInCache(f)
              storeDealerDetailsInCache(f, dealerDetails.traderBusinessName)
            }
            case None => Future {
              Logger.error("failed to find dealer name in cache on submit, redirecting...")
              Redirect(routes.SetUpTradeDetails.present)
            }
          }
      )
    }
  }

  def storeDealerDetailsInCache(model: BusinessChooseYourAddressModel, dealerName: String) = {
    val lookedUpAddress = addressLookupService.fetchAddressForUprn(model.uprnSelected)

    lookedUpAddress.map {
      case Some(addr) => {
        val dealerDetailsModel = DealerDetailsModel(dealerName = dealerName, dealerAddress = addr)
        storeDealerDetailsModelInCache(dealerDetailsModel)
        /* The redirect is done as the final step within the map so that:
         1) we are not blocking threads
         2) the browser does not change page before the future has completed and written to the cache.
         */
        Redirect(routes.VehicleLookup.present)
      }
      case None => BadRequest("The UPRN you submitted was not found by the web service") // TODO Consult with UX on look&feel, message and url of an error page
    }
  }
}