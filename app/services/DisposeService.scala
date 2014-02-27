package services

import scala.concurrent.Future
import models.domain.disposal_of_vehicle.{DisposeModel, DisposeResponse}

trait DisposeService {
  def invoke(cmd: DisposeModel): Future[DisposeResponse]
}
