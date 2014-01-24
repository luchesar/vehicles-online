package controllers.change_of_address

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._
import models.domain.{V5cSearchModel, AuthenticationModel}
import controllers.Mappings._

object Authentication extends Controller {

  def present = Action {
    Ok(html.change_of_address.authentication(authenticationForm))
  }

  def submit = Action { implicit request =>
    authenticationForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.change_of_address.authentication(formWithErrors)),
      ToAddress => Ok(views.html.change_of_address.v5c_search(v5cSearchForm))
    )
  }

  val authenticationForm = Form(
    mapping(
      "PIN" -> PIN(minLength = 6, maxLength = 6)
    )(AuthenticationModel.apply)(AuthenticationModel.unapply)
  )

  val v5cSearchForm = Form(
    mapping(
      "V5cReferenceNumber" -> V5cReferenceNumber(minLength = 11, maxLength = 11),
      "vehicleVRN" -> vehicleVRN(minLength = 2, maxLength = 8)
    )(V5cSearchModel.apply)(V5cSearchModel.unapply)
  )

}