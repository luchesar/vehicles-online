package controllers.change_of_address

import play.api.mvc._

object VerifyIdentity extends Controller {

  def present = Action { implicit request =>
    Ok(views.html.change_of_address.verify_identity())
  }

  def submit = Action {
    Redirect(routes.AreYouRegistered.present)
  }
}