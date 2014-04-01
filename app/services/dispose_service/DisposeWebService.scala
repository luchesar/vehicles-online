package services.dispose_service

import scala.concurrent.Future
import play.api.libs.ws.Response
import models.domain.disposal_of_vehicle.DisposeRequest

trait DisposeWebService {
  def callVehicleLookupService(request: DisposeRequest): Future[Response]
}
