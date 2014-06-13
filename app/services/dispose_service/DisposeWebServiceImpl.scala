package services.dispose_service

import com.google.inject.Inject
import common.LogFormats
import models.domain.disposal_of_vehicle.DisposeRequest
import play.api.libs.json.Json
import play.api.libs.ws.{Response, WS}
import services.HttpHeaders
import utils.helpers.Config

import scala.concurrent.Future

final class DisposeWebServiceImpl @Inject()(config: Config)  extends DisposeWebService {
  private val endPoint: String = s"${config.disposeVehicleMicroServiceBaseUrl}/vehicles/dispose/v1"
  private val requestTimeout: Int = config.disposeMsRequestTimeout

  override def callDisposeService(request: DisposeRequest): Future[Response] = {

    val vrm = LogFormats.anonymize(request.registrationNumber)
    val refNo = LogFormats.anonymize(request.referenceNumber)
    val postcode = LogFormats.anonymize(request.traderAddress.postCode)

    //Logger.debug(s"Calling dispose vehicle micro-service with $refNo $vrm $postcode ${request.keeperConsent} ${request.prConsent} ${request.mileage}")//request object: $request on $endPoint")
    WS.
      url(endPoint).
      withHeaders(HttpHeaders.TrackingId -> request.trackingId).
      withRequestTimeout(requestTimeout).
      post(Json.toJson(request))
  }
}
