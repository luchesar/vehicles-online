package controllers.change_of_address

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.domain.change_of_address._
import controllers.Mappings._

object Authentication extends Controller {
  val authenticationForm = Form( // TODO Should we move forms into a separate file.
    mapping(
      "PIN" -> PIN(minLength = 6, maxLength = 6)
    )(AuthenticationModel.apply)(AuthenticationModel.unapply)
  )

  val v5cSearchForm = Form(
    mapping(
      "V5cReferenceNumber" -> V5cReferenceNumber(),
      "V5CRegistrationNumber" -> V5CRegistrationNumber(minLength = 2, maxLength = 8)
    )(V5cSearchModel.apply)(V5cSearchModel.unapply)
  )

  def present = Action { implicit request =>
    Ok(views.html.change_of_address.authentication(authenticationForm))
  }

  def submit = Action { implicit request =>
    authenticationForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.change_of_address.authentication(formWithErrors)),
      _ => Redirect(routes.V5cSearch.present)
    )
  }

}