package services.dispose_service

import models.domain.disposal_of_vehicle.DisposeRequest
import play.api.libs.ws.Response
import scala.concurrent.Future

// TODO Do we still need this abstraction, now the code base is more mockable?
trait DisposeWebService {
  def callDisposeService(request: DisposeRequest, trackingId: String): Future[Response]
}