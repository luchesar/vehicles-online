package controllers.change_of_address


import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._

object KeeperStatus extends Controller {

  def present = Action {
    Ok(html.change_of_address.keeper_status())
  }

  def submit = Action {
    Redirect(routes.KeeperStatus.present) //TODO Change this to move to P3
  }

}