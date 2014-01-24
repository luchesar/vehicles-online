package controllers.change_of_address


import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._


object BeforeYouStart extends Controller {

  def present = Action {
    Ok(html.change_of_address.before_you_start())
  }

  def submit = Action {
    Redirect(routes.KeeperStatus.present)
  }

}
