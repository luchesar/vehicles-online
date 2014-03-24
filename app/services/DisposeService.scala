package services

import scala.concurrent.Future
import models.domain.disposal_of_vehicle.{DisposeRequest, DisposeResponse}

trait DisposeService {
  def invoke(cmd: DisposeRequest): Future[DisposeResponse]
}
