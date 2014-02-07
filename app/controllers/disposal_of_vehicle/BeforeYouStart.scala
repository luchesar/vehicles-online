package controllers.disposal_of_vehicle

import play.api.mvc._

object BeforeYouStart extends Controller {

  def present = Action {
    implicit request =>
      Ok(views.html.disposal_of_vehicle.before_you_start())
  }

  def submit = Action {
    Redirect(routes.BeforeYouStart.present)
  }

}