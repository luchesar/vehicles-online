package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.{VehicleDetailsModel, DealerDetailsModel, DisposeConfirmationFormModel, DisposeModel}
import app.DisposalOfVehicle.DisposeConfirmation._
import controllers.disposal_of_vehicle.Helpers._
import play.api.Logger

object DisposeConfirmation extends Controller {

  val disposeConfirmationForm = Form(
    mapping(
      emailAddressId -> text
    )(DisposeConfirmationFormModel.apply)(DisposeConfirmationFormModel.unapply)
  )

  def present = Action {
    implicit request => {
      (fetchDealerDetailsFromCache, fetchDisposeFormModelFromCache, fetchVehicleDetailsFromCache) match {
        case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails)) =>
          val disposeModel = fetchData(dealerDetails, vehicleDetails)
          Ok(views.html.disposal_of_vehicle.dispose_confirmation(disposeModel, disposeConfirmationForm, disposeFormModel))
        case _ => Redirect(routes.SetUpTradeDetails.present) // TODO write controller and integration tests for re-routing when not logged in.
      }
    }
  }

  def submit = Action {
    implicit request => {
      disposeConfirmationForm.bindFromRequest.fold(
        formWithErrors => {
          (fetchDealerDetailsFromCache, fetchDisposeFormModelFromCache, fetchVehicleDetailsFromCache)  match {
            case (Some(dealerDetails), Some(disposeFormModel), Some(vehicleDetails)) =>
              val disposeModel = fetchData(dealerDetails, vehicleDetails)
              BadRequest(views.html.disposal_of_vehicle.dispose_confirmation(disposeModel, formWithErrors, disposeFormModel))
            case _ => Redirect(routes.SetUpTradeDetails.present) // TODO write controller and integration tests for re-routing when not logged in.
          }
        },
        f => {Logger.debug(s"Form submitted email address = <<${f.emailAddress}>>"); Ok("success")}
      )
    }
  }

  private def fetchData(dealerDetails: DealerDetailsModel, vehicleDetails: VehicleDetailsModel): DisposeModel  = {
    DisposeModel(vehicleMake = vehicleDetails.vehicleMake,
      vehicleModel = vehicleDetails.vehicleModel,
      keeperName = vehicleDetails.keeperName,
      keeperAddress = vehicleDetails.keeperAddress,
      dealerName = dealerDetails.dealerName,
      dealerAddress = dealerDetails.dealerAddress)
  }
}