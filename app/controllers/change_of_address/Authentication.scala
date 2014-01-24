package controllers.change_of_address

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._
import models.domain.AuthenticationModel
import controllers.Mappings._

object Authentication extends Controller {

  def present = Action {
    Ok(html.change_of_address.authentication(authenticationForm))
  }

  def submit = Action { implicit request =>
    authenticationForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.change_of_address.authentication(formWithErrors)),
      ToAddress => Ok(views.html.change_of_address.authentication(authenticationForm)) //TODO change this to the next page with the next form when developed
    )
  }

  val authenticationForm = Form(
    mapping(
      "PIN" -> PIN(minLength = 6, maxLength = 6)
    )(AuthenticationModel.apply)(AuthenticationModel.unapply)
  )

}