package controllers.change_of_address

import play.api.mvc._
import models.domain.change_of_address.LoginConfirmationModel
import play.api.cache.Cache
import play.api.Play.current
import controllers.change_of_address.Helpers._

object LoginConfirmation extends Controller {

  def present = Action {
    implicit request =>
      userLoginCredentials() match {
        case Some(loginConfirmationModel) => Ok(views.html.change_of_address.login_confirmation(loginConfirmationModel))
        case None => Redirect(routes.AreYouRegistered.present)
      }
  }

  def submit = Action {
    Redirect(routes.Authentication.present)
  }

  private def fetchData(): Option[LoginConfirmationModel] = {
    val key = mappings.LoginConfirmation.key
    val result = Cache.getAs[LoginConfirmationModel](key)
    result
  }
}