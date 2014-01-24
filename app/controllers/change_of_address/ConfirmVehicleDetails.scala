package controllers.change_of_address


import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._


object ConfirmVehicleDetails extends Controller {

  def present = Action {
    Ok(html.change_of_address.confirm_vehicle_details())
  }

  def submit = Action {
    Redirect(routes.ConfirmVehicleDetails.present)
  }

}
