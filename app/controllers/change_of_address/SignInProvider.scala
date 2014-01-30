package controllers.change_of_address

import play.api.mvc._

object SignInProvider extends Controller {

  def present = Action { implicit request =>
    Ok(views.html.change_of_address.sign_in_provider())
  }

  def submit = Action {
    Redirect(routes.LoginPage.present)
  }

}