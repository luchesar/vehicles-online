package services.dispose_service

import models.domain.disposal_of_vehicle.DisposeRequest
import scala.concurrent.Future
import play.api.libs.ws.{WS, Response}
import play.api.libs.json.Json
import utils.helpers.Config
import play.api.Logger
import com.google.inject.Inject

final class DisposeWebServiceImpl @Inject()(config: Config)  extends DisposeWebService {
  private val endPoint: String = s"${config.disposeVehicleMicroServiceBaseUrl}/vehicles/dispose/v1"
  private val requestTimeout: Int = config.disposeMsRequestTimeout

  override def callDisposeService(request: DisposeRequest): Future[Response] = {
    Logger.debug(s"Calling dispose vehicle micro-service on $endPoint with request object: $request...")
    WS.
      url(endPoint).
      withRequestTimeout(requestTimeout).
      post(Json.toJson(request))
  }
}
