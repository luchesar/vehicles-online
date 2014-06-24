package services.dispose_service

import scala.concurrent.Future
import models.domain.disposal_of_vehicle.{DisposeRequest, DisposeResponse}

trait DisposeService {
  def invoke(cmd: DisposeRequest, trackingId: String): Future[(Int, Option[DisposeResponse])]
}
