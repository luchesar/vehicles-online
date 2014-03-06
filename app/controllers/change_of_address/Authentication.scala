package controllers.change_of_address

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import controllers.change_of_address.Helpers._
import models.domain.change_of_address.AuthenticationModel
import models.domain.change_of_address.V5cSearchModel
import mappings.change_of_address.Pin
import Pin._
import mappings.common.{ReferenceNumber, Postcode}
import mappings.common.Postcode._
import ReferenceNumber._
import mappings.common.{ReferenceNumber, RegistrationNumber}
import RegistrationNumber._

object Authentication extends Controller {
  val authenticationForm = Form(
    mapping(
      "PIN" -> pin(minLength = 6, maxLength = 6)
    )(AuthenticationModel.apply)(AuthenticationModel.unapply)
  )

  val v5cSearchForm = Form(
    mapping(
      ReferenceNumber.key -> referenceNumber(minLength = ReferenceNumber.minLength, maxLength = ReferenceNumber.maxLength),
      RegistrationNumber.key -> registrationNumber(minLength = RegistrationNumber.minLength, maxLength = RegistrationNumber.maxLength)
    )(V5cSearchModel.apply)(V5cSearchModel.unapply)
  )

  def present = Action { implicit request =>
    isUserLoggedIn match {
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