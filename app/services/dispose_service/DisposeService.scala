package services.dispose_service

import models.domain.disposal_of_vehicle.{DisposeRequest, DisposeResponse}
import scala.concurrent.Future

trait DisposeService {
  def invoke(cmd: DisposeRequest, trackingId: String): Future[(Int, Option[DisposeResponse])]
}