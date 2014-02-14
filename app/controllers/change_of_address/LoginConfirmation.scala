package controllers.change_of_address

import play.api.mvc._
import controllers.change_of_address.Helpers._

object LoginConfirmation extends Controller {

  def present = Action {
    implicit request =>
      userLoginCredentials() match {
        case Some(loginConfirmationModel) => Ok(views.html.change_of_address.login_confirmation(loginConfirmationModel))
        case None => Redirect(routes.AreYouRegistered.present)
      }
  }

  def submit = Action {
    Redirect(routes.Authentication.present)
  }
}