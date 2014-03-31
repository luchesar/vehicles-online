package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import mappings.common.{ReferenceNumber, RegistrationNumber, Consent}
import ReferenceNumber._
import RegistrationNumber._
import mappings.disposal_of_vehicle.VehicleLookup._
import Consent._
import models.domain.disposal_of_vehicle.{VehicleDetailsRequest, VehicleDetailsModel, VehicleLookupFormModel}
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import com.google.inject.Inject
import controllers.disposal_of_vehicle.Helpers._
import controllers.disposal_of_vehicle.Helpers.{storeVehicleDetailsInCache, storeVehicleLookupFormModelInCache}

class VehicleLookup @Inject()(webService: services.VehicleLookupService) extends Controller {

  val vehicleLookupForm = Form(
    mapping(
      referenceNumberId -> referenceNumber,
      registrationNumberId -> registrationNumber,
      consentId -> consent
    )(VehicleLookupFormModel.apply)(VehicleLookupFormModel.unapply)
  )

  def present = Action { implicit request =>
    fetchDealerDetailsFromCache match {
      case Some(dealerDetails) => Ok(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, vehicleLookupForm))
      case None => Redirect(routes.SetUpTradeDetails.present)
    }
  }

  def submit = Action.async { implicit request =>
    vehicleLookupForm.bindFromRequest.fold(
      formWithErrors =>
        Future {
          fetchDealerDetailsFromCache match {
            case Some(dealerDetails) => BadRequest(views.html.disposal_of_vehicle.vehicle_lookup(dealerDetails, formWithErrors))
            case None => Redirect(routes.SetUpTradeDetails.present)
          }
        },
      f => {
        val modelWithoutSpaces = f.copy(registrationNumber = f.registrationNumber.replace(" ","")) // DE7 Strip spaces from input as it is not allowed in the micro-service.
        lookupVehicle(webService, modelWithoutSpaces)
      }
    )
  }

  def back = Action { implicit request =>
    fetchDealerDetailsFromCache match {
      case Some(dealerDetails) =>
        if (dealerDetails.dealerAddress.uprn.isDefined) Redirect(routes.BusinessChooseYourAddress.present)
        else Redirect(routes.EnterAddressManually.present)
      case None => Redirect(routes.SetUpTradeDetails.present)
    }
  }

  private def lookupVehicle(webService: services.VehicleLookupService, model: VehicleLookupFormModel): Future[SimpleResult] = {
    webService.invoke(buildMicroServiceRequest(model)).map { resp =>
      Logger.debug(s"VehicleLookup Web service call successful - response = ${resp}")
      // TODO Don't save these two models, instead we need a combined model that has what the user entered into the form plus the micro-service response.
      storeVehicleLookupFormModelInCache(model)
      if (resp.success) {
        storeVehicleDetailsInCache(VehicleDetailsModel.fromDto(resp.vehicleDetailsDto))
        Redirect(routes.Dispose.present)
      }
      else Redirect(routes.VehicleLookupFailure.present)
    }.recover {
      case e: Throwable => {
        Logger.debug(s"Web service call failed. Exception: ${e}")
        BadRequest("The remote server didn't like the request.")
      }
    }
  }

  private def buildMicroServiceRequest(formModel: VehicleLookupFormModel):VehicleDetailsRequest = {
    VehicleDetailsRequest(referenceNumber = formModel.referenceNumber, registrationNumber = formModel.registrationNumber)
  }

}


