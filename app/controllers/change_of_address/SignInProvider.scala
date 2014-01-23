package controllers.change_of_address

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._

object SignInProvider extends Controller {

  def present = Action {
    Ok(html.change_of_address.sign_in_provider())
  }

  def submit = Action {
    Redirect(routes.SignInProvider.present)
  }

}