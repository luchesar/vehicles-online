package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.{DealerDetailsModel, BusinessChooseYourAddressModel}
import mappings.disposal_of_vehicle.BusinessAddressSelect._
import mappings.common.DropDown
import DropDown._
import controllers.disposal_of_vehicle.Helpers._
import play.api.Logger
import play.api.Play.current
import mappings.disposal_of_vehicle.DealerDetails
import javax.inject.Inject
import scala.concurrent.Await
import scala.concurrent.duration.Duration


class BusinessChooseYourAddress @Inject()(addressLookupService: services.AddressLookupService) extends Controller {
  private lazy val fetchAddresses = {
    /* Needs to be a lazy val otherwise when the page is IoC'd the form will execute it, so if you were jumping to the
     page with nothing in the cache it will blow up in the constructor before it gets to the code to redirect to another page.
     */
    val postcode = fetchTraderDetailsFromCache match {
      case Some(setupTradeDetailsModel) => setupTradeDetailsModel.traderPostcode
      case None => ??? //"TEST"
    }

    val addresses = addressLookupService.fetchAddressesForPostcode(postcode)

    Await.result(addresses, Duration.Inf) // TODO don't use Await, use controller action Action.async
  }

  val form = Form(
    mapping(
      /* We cannot apply constraints to this drop down as it is populated by web call to an address lookup service.
      Validation is done when we make a second web call with the UPRN, so if a bad guy is injecting a non-existant UPRN
      then it will fail at that step instead */
      addressSelectId -> dropDown
    )(BusinessChooseYourAddressModel.apply)(BusinessChooseYourAddressModel.unapply)
  )

  def present = Action {
    implicit request =>
      fetchTraderDetailsFromCache match {
        case Some(dealerDetails) => Ok(views.html.disposal_of_vehicle.business_choose_your_address(form, dealerDetails.traderBusinessName, fetchAddresses))
        case None => Redirect(routes.SetUpTradeDetails.present)
      }
  }

  def submit = Action {
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
            case Some(dealerDetails) => BadRequest(views.html.disposal_of_vehicle.business_choose_your_address(formWithErrors, dealerDetails.traderBusinessName, fetchAddresses))
            case None => {
              Logger.error("failed to find dealer name in cache for formWithErrors, redirecting...")
              Redirect(routes.SetUpTradeDetails.present)
            }
          },
        f =>
          fetchTraderDetailsFromCache match {
            case Some(dealerDetails) => {
              storeDealerDetailsInCache(f, dealerDetails.traderBusinessName)
              Redirect(routes.VehicleLookup.present)
            }
            case None => {
              Logger.error("failed to find dealer name in cache on submit, redirecting...")
              Redirect(routes.SetUpTradeDetails.present)
            }
          }
      )
    }
  }

  def storeDealerDetailsInCache(model: BusinessChooseYourAddressModel, dealerName: String) = {
    val lookedUpAddress = addressLookupService.lookupAddress(model.uprnSelected)
    val key = DealerDetails.cacheKey
    val value = DealerDetailsModel(dealerName = dealerName, dealerAddress = lookedUpAddress)
    play.api.cache.Cache.set(key, value)
    Logger.debug(s"BusinessChooseYourAddress stored data in cache: key = $key, value = ${value}")
  }
}