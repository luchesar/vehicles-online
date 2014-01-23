package controllers.change_of_address

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._

object VerifyIdentity extends Controller {

  def present = Action {
    Ok(html.change_of_address.verify_identity())
  }

  def submit = Action {
    Redirect(routes.VerifyIdentity.present) //TODO Change this to move to P4
  }

}