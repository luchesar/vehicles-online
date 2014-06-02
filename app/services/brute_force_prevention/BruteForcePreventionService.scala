package services.brute_force_prevention

import models.domain.disposal_of_vehicle.BruteForcePreventionViewModel
import scala.concurrent.Future

trait BruteForcePreventionService {
  def isVrmLookupPermitted(vrm: String): Future[Option[(BruteForcePreventionViewModel)]]
}