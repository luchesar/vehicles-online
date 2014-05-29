package services.brute_force_prevention

import models.domain.disposal_of_vehicle.{PostcodeToAddressResponse, AddressViewModel}
import scala.concurrent.Future
import play.api.libs.ws.Response

trait BruteForcePreventionService {
  def vrmLookupPermitted(vrm: String): Future[Boolean]
}