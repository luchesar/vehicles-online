package services

import models.domain.change_of_address.{V5cSearchConfirmationModel, V5cSearchResponse, V5cSearchModel}
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.Config

class V5cSearchWebServiceImpl() extends V5cSearchWebService {
  implicit val writeV5cSearch = Json.writes[V5cSearchModel]
  implicit val v5cSearchConfirmationModel = Json.format[V5cSearchConfirmationModel]
  implicit val v5cSearchResponse = Json.format[V5cSearchResponse]

  override def invoke(cmd: V5cSearchModel): Future[V5cSearchResponse] = {
    val endPoint = s"${Config.microServiceBaseUrl}/vehicles/v5c-search"
    Logger.debug(s"Calling V5C micro service on ${endPoint}...")
    val futureOfResponse = WS.url(endPoint).post(Json.toJson(cmd))

    futureOfResponse.map { resp =>
      Logger.debug(s"Http response code from V5C micro service was: ${resp.status}")
      resp.json.as[V5cSearchResponse]
    }
  }
}
