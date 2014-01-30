package controllers.change_of_address

import play.api.mvc._

object BeforeYouStart extends Controller {

  def present = Action {
    implicit request => // TODO change intellij config so that implicit lambda will be on the same line as the previous open curly brace.
      Ok(views.html.change_of_address.before_you_start())
  }

  def submit = Action {
    Redirect(routes.KeeperStatus.present)
  }

}