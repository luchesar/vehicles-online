package services

import models.domain.change_of_address.{V5cSearchResponse, V5cSearchModel}
import play.api.libs.json.Json
import play.api.libs.ws.WS
import play.api.Logger
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import app.JsonSupport._
import utils.helpers.Environment

class V5cSearchWebServiceImpl() extends V5cSearchWebService {
  override def invoke(cmd: V5cSearchModel): Future[V5cSearchResponse] = {
    val endPoint = s"${Environment.microServiceUrlBase}/vehicles/v5c-search"
    Logger.debug(s"Calling V5C micro service on ${endPoint}...")
    val futureOfResponse = WS.url(endPoint).post(Json.toJson(cmd))

    futureOfResponse.map { resp =>
      Logger.debug(s"Http response code from V5C micro service was: ${resp.status}")
      resp.json.as[V5cSearchResponse]
    }
  }
}
