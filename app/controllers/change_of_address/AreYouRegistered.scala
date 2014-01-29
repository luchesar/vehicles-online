package controllers.change_of_address

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._

object AreYouRegistered extends Controller {

  def present = Action { implicit request =>
    Ok(html.change_of_address.are_you_registered())
  }

  def submit = Action {
    Redirect(routes.SignInProvider.present)
  }

}