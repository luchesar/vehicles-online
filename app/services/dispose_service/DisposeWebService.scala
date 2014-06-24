package services.dispose_service

import scala.concurrent.Future
import play.api.libs.ws.Response
import models.domain.disposal_of_vehicle.DisposeRequest

trait DisposeWebService {
  def callDisposeService(request: DisposeRequest, trackingId: String): Future[Response]
}
