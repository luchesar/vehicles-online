package controllers.change_of_address

import play.api.data.Forms._
import play.api.mvc._
import views._
import play.api.data.Form
import models.domain.change_of_address.LoginConfirmationModel
import models.domain.change_of_address.Address

object LoginConfirmation extends Controller {
  
  def present = Action {
    Ok(html.change_of_address.login_confirmation(fetchData()))
  }

  def submit = Action {
    Redirect(routes.Authentication.present)
//    Ok("success")
  }

  def fetchData(): LoginConfirmationModel = {
    LoginConfirmationModel("Roger", "Booth", "21/05/1977", Address("115 Park Avenue", "SA6 8HY", "United Kingdom"))
  }
  
}