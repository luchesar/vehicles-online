package services.dispose_service

import models.domain.disposal_of_vehicle.DisposeRequest
import scala.concurrent.Future
import play.api.libs.ws.{WS, Response}
import play.api.libs.json.Json
import utils.helpers.Config
import play.api.Logger

class DisposeWebServiceImpl extends DisposeWebService {
  val endPoint = s"${Config.microServiceBaseUrl}/vehicles/dispose/v1"
  val requestTimeout = Config.ordnanceSurveyRequestTimeout.toInt // TODO make a new value for this micro-service call.

  override def callDisposeService(request: DisposeRequest): Future[Response] = {
    Logger.debug(s"Calling dispose vehicle micro service on $endPoint with request object: $request...")
    WS.
      url(endPoint).
      withRequestTimeout(requestTimeout).
      post(Json.toJson(request))
  }
}
