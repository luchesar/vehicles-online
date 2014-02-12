package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import models.domain.disposal_of_vehicle.DisposeFormModel

import models.domain.disposal_of_vehicle.DisposeModel
import models.domain.common.Address
import mappings.disposal_of_vehicle.Dispose._
import mappings.Consent._
import mappings.Mileage._
import mappings.DayMonthYear._
import constraints.DayMonthYear._
import controllers.disposal_of_vehicle.Helpers._
import models.domain.disposal_of_vehicle.DisposeFormModel
import scala.Some
import models.domain.disposal_of_vehicle.DisposeModel
import models.domain.common.Address

object Dispose extends Controller {

  val disposeForm = Form(
    mapping(
      consentId -> consent,
      mileageId -> mileage(),
      dateOfDisposalId -> dayMonthYear.verifying(rules)
    )(DisposeFormModel.apply)(DisposeFormModel.unapply)
  )

  def present = Action {
    implicit request => {
      fetchDealerNameFromCache match {
        case Some(traderBusinessName) => {
          // Pre-populate the form so that the consent checkbox is ticked and today's date is displayed in the date control
          val filledForm = disposeForm.fill(DisposeFormModel(consent = true, dateOfDisposal = models.DayMonthYear.today))
          Ok(views.html.disposal_of_vehicle.dispose(fetchData, filledForm))
        }
        case None => Redirect(routes.SetUpTradeDetails.present) // TODO write controller and integration tests for re-routing when not logged in.
      }
    }
  }

  def submit = Action {
    implicit request => {
      Logger.debug("Submitted dispose form...")
      disposeForm.bindFromRequest.fold(
        formWithErrors => {
          fetchDealerNameFromCache match {
            case Some(traderBusinessName) => BadRequest(views.html.disposal_of_vehicle.dispose(fetchData, formWithErrors))
            case None => Redirect(routes.SetUpTradeDetails.present) // TODO write controller and integration tests for re-routing when not logged in.
          }
        },
        f => {
          Logger.debug(s"Dispose form submitted - consent = ${f.consent}, mileage = ${f.mileage}, disposalDate = ${f.dateOfDisposal}")
          Redirect(routes.DisposeConfirmation.present)}
      )
    }
  }

  private def fetchData: DisposeModel  = {
    DisposeModel(vehicleMake = "PEUGEOT",
      vehicleModel = "307 CC",
      keeperName = "Mrs Anne Shaw",
      keeperAddress = Address("1 The Avenue", Some("Earley"), Some("Reading"), None, "RG12 6HT"),
      dealerName = "Dealer name",
      dealerAddress = Address("Address line 1", Some("Address line 2"), Some("Address line 3"), None, "Postcode"))
  }
}