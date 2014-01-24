package controllers.change_of_address

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._

object Authentication extends Controller {

  def present = Action {
    Ok(html.change_of_address.authentication())
  }

  def submit = Action {
    Redirect(routes.Authentication.present) //TODO change to next page
  }

}