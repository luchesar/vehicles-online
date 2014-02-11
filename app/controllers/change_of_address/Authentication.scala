package controllers.change_of_address

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import controllers.Mappings._
import controllers.change_of_address.Helpers._
import models.domain.change_of_address.AuthenticationModel
import models.domain.change_of_address.V5cSearchModel
import mappings.Pin._
import mappings.{V5cRegistrationNumber, V5cReferenceNumber}
import constraints.V5cReferenceNumber._

object Authentication extends Controller {
  val authenticationForm = Form(
    mapping(
      "PIN" -> pin(minLength = 6, maxLength = 6)
    )(AuthenticationModel.apply)(AuthenticationModel.unapply)
  )

  val v5cSearchForm = Form(
    mapping(
      V5cReferenceNumber.key -> v5cReferenceNumber(minLength = V5cReferenceNumber.minLength, maxLength = V5cReferenceNumber.maxLength),
      V5cRegistrationNumber.key -> v5CRegistrationNumber(minLength = V5cRegistrationNumber.minLength, maxLength = V5cRegistrationNumber.maxLength),
      Postcode.key -> Postcode(minLength = Postcode.minLength, maxLength = Postcode.maxLength)
    )(V5cSearchModel.apply)(V5cSearchModel.unapply)
  )

  def present = Action { implicit request =>
    isUserLoggedIn() match {
      case true =>  Ok(views.html.change_of_address.authentication(authenticationForm))
      case false => Redirect(routes.AreYouRegistered.present)
    }
  }

  def submit = Action { implicit request =>
    authenticationForm.bindFromRequest.fold(
      formWithErrors => BadRequest(views.html.change_of_address.authentication(formWithErrors)),
      _ => Redirect(routes.VehicleSearch.present)
    )
  }

}