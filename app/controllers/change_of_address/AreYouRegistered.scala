package controllers.change_of_address

import play.api.mvc._

object AreYouRegistered extends Controller {

  def present = Action {
    implicit request =>
      Ok(views.html.change_of_address.are_you_registered())
  }

  def submit = Action {
    Redirect(routes.SignInProvider.present)
  }

}