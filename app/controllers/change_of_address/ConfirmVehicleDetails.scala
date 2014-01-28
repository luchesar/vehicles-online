package controllers.change_of_address


import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._
import models.domain.change_of_address.V5cSearchConfirmationModel


object ConfirmVehicleDetails extends Controller {

  def present = Action { implicit request =>
    Ok(html.change_of_address.confirm_vehicle_details(fetchData()))
  }

  def submit = Action {
    Redirect(routes.ConfirmVehicleDetails.present)
  }

  def fetchData(): V5cSearchConfirmationModel = {
    V5cSearchConfirmationModel("BD 54 XZP", "PEUGEOT", "307 CC", "01 11 2004", "28 07 2007")
  }

}
