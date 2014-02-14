package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.{DealerDetailsModel, BusinessChooseYourAddressModel}
import mappings.disposal_of_vehicle.BusinessAddressSelect._
import mappings.DropDown._
import controllers.disposal_of_vehicle.Helpers._
import play.api.Logger
import play.api.Play.current
import mappings.disposal_of_vehicle.DealerDetails
import javax.inject.{Singleton, Inject}

@Singleton
class BusinessChooseYourAddress @Inject() (addressLookupService: services.AddressLookupService) extends Controller {
  val fetchAddresses = addressLookupService.fetchAddress("TEST") // TODO pass in postcode submitted on the previous page.
  
  val businessChooseYourAddressForm = Form(
    mapping(
      addressSelectId -> dropDown(fetchAddresses)
    )(BusinessChooseYourAddressModel.apply)(BusinessChooseYourAddressModel.unapply)
  )

  def present = Action {
    implicit request =>
    {
      fetchDealerNameFromCache match {
        case Some(traderBusinessName) => Ok(views.html.disposal_of_vehicle.business_choose_your_address(businessChooseYourAddressForm, traderBusinessName, fetchAddresses))
        case None => Redirect(routes.SetUpTradeDetails.present)
      }
    }
  }

  def submit = Action {
    implicit request => {
      businessChooseYourAddressForm.bindFromRequest.fold(
        formWithErrors => {
          fetchDealerNameFromCache match {
            case Some(traderBusinessName) => BadRequest(views.html.disposal_of_vehicle.business_choose_your_address(formWithErrors, traderBusinessName, fetchAddresses))
            case None => {
              Logger.error("failed to find dealer name in cache for formWithErrors, redirecting...")
              Redirect(routes.SetUpTradeDetails.present)
            }
          }
        },
        f => {
          fetchDealerNameFromCache match {
            case Some(dealerName) => {
              storeDealerDetailsInCache(f, dealerName)
              Redirect(routes.VehicleLookup.present)
            }
            case None => {
              Logger.error("failed to find dealer name in cache on submit, redirecting...")
              Redirect(routes.SetUpTradeDetails.present)
            }
          }
        }
      )
    }
  }

  private def storeDealerDetailsInCache(f: BusinessChooseYourAddressModel, dealerName: String) = {
    val key = DealerDetails.cacheKey
    val value = DealerDetailsModel(dealerName = dealerName, dealerAddress = addressLookupService.lookupAddress(f.addressSelected))
    play.api.cache.Cache.set(key, value)
    Logger.debug(s"BusinessChooseYourAddress stored data in cache: key = $key, value = ${value}")
  }
}