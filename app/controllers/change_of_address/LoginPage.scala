package controllers.change_of_address

import play.api.data.Forms._
import play.api.mvc._
import views._
import play.api.data.Form
import models.domain.change_of_address.LoginPageModel

object LoginPage extends Controller {

  val loginPageForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginPageModel.apply)(LoginPageModel.unapply)
  )  
  
  def present = Action {
    Ok(html.change_of_address.login_page(loginPageForm))
  }

  def submit = Action {
    Redirect(routes.LoginConfirmation.present)
  }

}