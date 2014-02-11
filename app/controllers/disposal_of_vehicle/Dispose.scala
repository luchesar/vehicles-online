package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.Form
import play.api.data.Forms._
import play.api.Logger
import controllers.Mappings._
import models.domain.disposal_of_vehicle.DisposeFormModel

import models.domain.disposal_of_vehicle.DisposeModel
import models.domain.common.Address
import app.DisposalOfVehicle.Dispose._

object Dispose extends Controller {

  val disposeForm = Form(
    mapping(
      consentId -> consent,
      mileageId -> Mileage(minLength = 0, maxLength = 999999),
      dateOfDisposalId -> dayMonthYear.verifying(validDate)
    )(DisposeFormModel.apply)(DisposeFormModel.unapply)
  )

  def present = Action {
    implicit request => {
      // Pre-populate the form so that the consent checkbox is ticked and today's date is displayed in the date control
      val filledForm = disposeForm.fill(DisposeFormModel(consent = true, dateOfDisposal = models.DayMonthYear.today))
      Ok(views.html.disposal_of_vehicle.dispose(fetchData, filledForm))
    }
  }

  def submit = Action {
    implicit request => {
      Logger.debug("Submitted dispose form...")
      disposeForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.disposal_of_vehicle.dispose(fetchData, formWithErrors)),
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
      dealerName = "Car Giant",
      dealerAddress = Address("44 Hythe Road", Some("White City"), Some("London"), None, "NW10 6RJ"))
  }
}