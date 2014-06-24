package services.dispose_service

import play.api.Logger
import play.api.http.Status._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.Config
import models.domain.disposal_of_vehicle.{DisposeRequest, DisposeResponse}
import javax.inject.Inject
import common.LogFormats

final class DisposeServiceImpl @Inject()(config: Config, ws: DisposeWebService) extends DisposeService {
  override def invoke(cmd: DisposeRequest, trackingId: String): Future[(Int, Option[DisposeResponse])] = {
    val endPoint = s"${config.disposeVehicleMicroServiceBaseUrl}/vehicles/dispose/v1"

    val vrm = LogFormats.anonymize( cmd.registrationNumber)
    val refNo = LogFormats.anonymize(cmd.referenceNumber)
    val postcode = LogFormats.anonymize(cmd.traderAddress.postCode)

    Logger.debug(s"Calling dispose vehicle micro-service with $refNo $vrm $postcode ${cmd.keeperConsent} ${cmd.prConsent} ${cmd.mileage}")//request object: $cmd on $endPoint")

    ws.callDisposeService(cmd, trackingId).map {
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

