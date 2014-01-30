package controllers.change_of_address

import play.api.mvc._

object KeeperStatus extends Controller {

  def present = Action { implicit request =>
    Ok(views.html.change_of_address.keeper_status())
  }

  def submit = Action { 
    Redirect(routes.VerifyIdentity.present)
  }

}