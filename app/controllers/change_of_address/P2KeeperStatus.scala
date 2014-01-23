package controllers.change_of_address


import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._

object P2KeeperStatus extends Controller {

  def present = Action {
    Ok(html.change_of_address.p2keeperstatus())
  }

  def submit = Action {
    Ok(html.change_of_address.p2keeperstatus()) //TODO Change this to move to P3
  }

}