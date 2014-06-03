package services.dispose_service

import play.api.Logger
import play.api.http.Status._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import utils.helpers.Config
import models.domain.disposal_of_vehicle.{DisposeRequest, DisposeResponse}
import javax.inject.Inject

final class DisposeServiceImpl @Inject()(config: Config, ws: DisposeWebService) extends DisposeService {
  override def invoke(cmd: DisposeRequest): Future[(Int, Option[DisposeResponse])] = {
    val endPoint = s"${config.disposeVehicleMicroServiceBaseUrl}/vehicles/dispose/v1"
    Logger.debug(s"Calling dispose vehicle micro-service on $endPoint with request object: $cmd...")

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

