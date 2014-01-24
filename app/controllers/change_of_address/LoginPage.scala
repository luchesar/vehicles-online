package controllers.change_of_address

import play.api.data.Forms._
import play.api.mvc._
import views._
import play.api.data.Form
import models.domain.change_of_address.LoginPageModel
import play.api.i18n.Messages
import play.api.i18n.Lang

object LoginPage extends Controller {

  val loginPageForm = Form(
    mapping(
      "username" -> nonEmptyText,
      "password" -> nonEmptyText
    )(LoginPageModel.apply)(LoginPageModel.unapply)
  )  

  // Note that the request must be made implicit so play can extract the language header for I18N
  def present = Action { implicit request =>
    println("Preferred languages (in order) from browser: " + request.acceptLanguages.map(_.code).mkString(", "))
    Ok(html.change_of_address.login_page(loginPageForm))
  }

  def submit = Action {
    Redirect(routes.LoginConfirmation.present)
  }

}