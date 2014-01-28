package services

import models.domain.change_of_address.{V5cSearchResponse, V5cSearchModel}
import models.domain.change_of_address.{V5cSearchConfirmationModel, V5cSearchResponse, V5cSearchModel}
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

case class V5cSearchResponseWebService() extends WebService {
  override def invoke(cmd: V5cSearchModel): Future[V5cSearchResponse] = {
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
