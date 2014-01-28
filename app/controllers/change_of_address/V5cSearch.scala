package controllers.change_of_address

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import views._
import models.domain.change_of_address.{V5cSearchModel, V5cSearchResponse, V5cSearchConfirmationModel}
import controllers.Mappings._
import scala.concurrent.Future
import play.api.libs.json.Json
import play.api.Logger
import play.api.libs.ws.WS
import scala.concurrent.{ExecutionContext, Future, Await}
import ExecutionContext.Implicits.global

object V5cSearch extends Controller {

  def present = Action { implicit request =>
    Ok(html.change_of_address.v5c_search(v5cSearchForm, fetchData))
  }

  def submit = Action { implicit request =>
    v5cSearchForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(html.change_of_address.v5c_search(formWithErrors, fetchData()))
        },
        ToAddress => Redirect(routes.ConfirmVehicleDetails.present())
    )
  }

  val v5cSearchForm = Form( 
    mapping(
      "V5cReferenceNumber" -> V5cReferenceNumber(minLength = 11, maxLength = 11),
      "VehicleRegistrationNumber" -> vehicleVRN(minLength = 2, maxLength = 7)
    )(V5cSearchModel.apply)(V5cSearchModel.unapply)
  )

  def fetchData(): String = {
    "Roger Booth"
  }

  def invokeWebService(cmd: V5cSearchModel): Future[V5cSearchResponse] = {
    implicit val V5cSearch = Json.writes[V5cSearchModel]

    val futureOfResponse = WS
      .url("http://localhost:8080/vehicles/v5c-search").post(Json.toJson(cmd))

    futureOfResponse.map{ resp =>
      implicit val v5cSearchConfirmationModel = Json.reads[V5cSearchConfirmationModel]
      implicit val v5cSearchResponse = Json.reads[V5cSearchResponse]

      Logger.debug(s"******* http response code from microservice was: ${resp.status}")

      resp.json.as[V5cSearchResponse]
    }
  }
}