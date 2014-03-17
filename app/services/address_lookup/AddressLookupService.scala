package services.address_lookup

import models.domain.disposal_of_vehicle.AddressViewModel
import scala.concurrent.Future
import play.api.libs.ws.Response

trait AddressLookupService {
  def fetchAddressesForPostcode(postcode: String): Future[Seq[(String, String)]]
  def fetchAddressForUprn(uprn: String): Future[Option[AddressViewModel]]
}