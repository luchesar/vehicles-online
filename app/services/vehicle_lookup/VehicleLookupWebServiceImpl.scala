package services.vehicle_lookup

import scala.concurrent.Future
import play.api.libs.ws.{WS, Response}
import models.domain.disposal_of_vehicle.VehicleDetailsRequest
import play.api.libs.json.Json
import utils.helpers.Config
import play.api.Logger
import com.google.inject.Inject
import common.LogFormats

final class VehicleLookupWebServiceImpl @Inject()(config: Config) extends VehicleLookupWebService {
  private val endPoint: String = s"${config.vehicleLookupMicroServiceBaseUrl}/vehicles/lookup/v1/dispose"

  override def callVehicleLookupService(request: VehicleDetailsRequest): Future[Response] = {

    val vrm = LogFormats.anonymize(request.registrationNumber)
    val refNo = LogFormats.anonymize(request.referenceNumber)

    Logger.debug(s"Calling vehicle lookup micro-service with request $refNo $vrm") //object: $request on ${endPoint}")
    WS.url(endPoint).post(Json.toJson(request))
  }
}
