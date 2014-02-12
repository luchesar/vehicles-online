package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.{DealerDetailsModel, DisposeConfirmationFormModel, DisposeModel}
import models.domain.common.Address
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
      fetchDealerDetailsFromCache match {
        case Some(dealerDetails) => Ok(views.html.disposal_of_vehicle.dispose_confirmation(fetchData(dealerDetails), disposeConfirmationForm))
        case None => Redirect(routes.SetUpTradeDetails.present) // TODO write controller and integration tests for re-routing when not logged in.
      }
    }
  }

  def submit = Action {
    implicit request => {
      disposeConfirmationForm.bindFromRequest.fold(
        formWithErrors => {
          fetchDealerDetailsFromCache match {
            case Some(dealerDetails) => BadRequest(views.html.disposal_of_vehicle.dispose_confirmation(fetchData(dealerDetails), formWithErrors))
            case None => Redirect(routes.SetUpTradeDetails.present) // TODO write controller and integration tests for re-routing when not logged in.
          }
        },
        f => {Logger.debug(s"Form submitted email address = <<${f.emailAddress}>>"); Ok("success")}
      )
    }
  }

  private def fetchData(dealerDetails: DealerDetailsModel): DisposeModel  = {
    DisposeModel(vehicleMake = "PEUGEOT",
      vehicleModel = "307 CC",
      keeperName = "Mrs Anne Shaw",
      keeperAddress = Address("1 The Avenue", Some("Earley"), Some("Reading"), None, "RG12 6HT"),
      dealerName = dealerDetails.dealerName,
      dealerAddress = dealerDetails.dealerAddress)
  }
}