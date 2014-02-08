package controllers.disposal_of_vehicle

import play.api.mvc._
import play.api.data.{Mapping, Form}
import play.api.data.Forms._
import models.domain.disposal_of_vehicle.{DisposeConfirmationFormModel, DisposeModel}
import models.domain.common.Address

object DisposeConfirmation extends Controller {

  val disposeConfirmationForm = Form(
    mapping(
      "emailAddress" -> text
    )(DisposeConfirmationFormModel.apply)(DisposeConfirmationFormModel.unapply)
  )

  def present = Action {
    implicit request =>
      Ok(views.html.disposal_of_vehicle.dispose_confirmation(fetchData, disposeConfirmationForm))
  }

  def submit = Action {
    implicit request => {
      println("Submitted dispose confirmation form ")
      disposeConfirmationForm.bindFromRequest.fold(
        formWithErrors => BadRequest(views.html.disposal_of_vehicle.dispose_confirmation(fetchData, formWithErrors)),
        f => {println(s"Form submitted email address = <<${f.emailAddress}>>"); Ok("success")} //Redirect(routes.VehicleLookup.present)
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