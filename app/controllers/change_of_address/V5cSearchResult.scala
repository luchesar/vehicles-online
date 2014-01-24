package controllers.change_of_address

import play.api.data.Forms._
import play.api.mvc._
import views._
import play.api.data.Form
import models.domain.change_of_address.LoginConfirmationModel
import models.domain.change_of_address.Address

object V5cSearchResult extends Controller {
  
  def present = Action {
    Ok(html.change_of_address.login_confirmation(fetchData()))
  }

  def submit = Action {
    Redirect(routes.LoginConfirmation.present) //TODO Change this to move to next page
//    Ok("success")
  }

  def fetchData(): LoginConfirmationModel = {
    LoginConfirmationModel("Roger", "Booth", "21/05/1977", Address("115 Park Avenue", "SA6 8HY", "United Kingdom"))
  }
  
}