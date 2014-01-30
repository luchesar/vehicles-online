package controllers.change_of_address

import play.api.data.Forms._
import play.api.mvc._
import views._
import play.api.data.Form
import models.domain.change_of_address.LoginConfirmationModel
import models.domain.change_of_address.Address
import play.api.cache.Cache
import controllers.Mappings
import scala.concurrent.{ExecutionContext, Future, Await}
import ExecutionContext.Implicits.global
import play.api.Play.current
import controllers.change_of_address.Helpers._

object LoginConfirmation extends Controller {

  def present = Action { implicit request =>
    userLoginCredentials() match {
      case Some(loginConfirmationModel) => Ok(html.change_of_address.login_confirmation(loginConfirmationModel))
      case None => Redirect(routes.LoginPage.present())
    }
  }

  def submit = Action {
    Redirect(routes.Authentication.present)
  }
}