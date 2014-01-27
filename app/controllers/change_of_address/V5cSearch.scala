package controllers.change_of_address

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._
import models.domain.V5cSearchModel
import controllers.Mappings._

object V5cSearch extends Controller {

  def present = Action { implicit request =>
    Ok(html.change_of_address.v5c_search(v5cSearchForm))
  }


  def submit = Action { implicit request =>
    v5cSearchForm.bindFromRequest.fold(
      formWithErrors => BadRequest(html.change_of_address.v5c_search(formWithErrors)),
        ToAddress => Redirect(routes.ConfirmVehicleDetails.present())
    )
  }

val v5cSearchForm = Form(
    mapping(
      "V5cReferenceNumber" -> V5cReferenceNumber(minLength = 11, maxLength = 11),
      "vehicleVRN" -> vehicleVRN(minLength = 2, maxLength = 7)
    )(V5cSearchModel.apply)(V5cSearchModel.unapply)
  )

}