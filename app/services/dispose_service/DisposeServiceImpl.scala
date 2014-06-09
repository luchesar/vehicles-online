package services.dispose_service

import play.api.Logger
import play.api.http.Status._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.Config
import models.domain.disposal_of_vehicle.{DisposeRequest, DisposeResponse}
import javax.inject.Inject
import mappings.disposal_of_vehicle.Logging

final class DisposeServiceImpl @Inject()(config: Config, ws: DisposeWebService) extends DisposeService {
  override def invoke(cmd: DisposeRequest): Future[(Int, Option[DisposeResponse])] = {
    val endPoint = s"${config.disposeVehicleMicroServiceBaseUrl}/vehicles/dispose/v1"

    val vrm = Logging.anonymize( cmd.registrationNumber)
    val refNo = Logging.anonymize(cmd.referenceNumber)
    val postcode = Logging.anonymize(cmd.traderAddress.postCode)

    Logger.debug(s"Calling dispose vehicle micro-service with $refNo $vrm $postcode ${cmd.keeperConsent} ${cmd.prConsent} ${cmd.mileage}")//request object: $cmd on $endPoint")

    ws.callDisposeService(cmd).map {
      resp =>
        Logger.debug(s"Http response code from dispose vehicle micro-service was: ${resp.status}")

        if (resp.status == OK){
          (resp.status, Option(resp.json.as[DisposeResponse])) 
        } else {
           (resp.status, None)
        }
    }
  }
}

