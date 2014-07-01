package services.dispose_service

import scala.concurrent.Future
import play.api.libs.ws.Response
import models.domain.disposal_of_vehicle.DisposeRequest

// TODO Do we still need this abstraction, now the code base is more mockable?
trait DisposeWebService {
  def callDisposeService(request: DisposeRequest, trackingId: String): Future[Response]
}