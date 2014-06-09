package services.address_lookup

import models.domain.disposal_of_vehicle.AddressViewModel
import scala.concurrent.Future
import common.ClientSideSession

trait AddressLookupService {
  def fetchAddressesForPostcode(postcode: String)
                               (implicit session: ClientSideSession): Future[Seq[(String, String)]]

  def fetchAddressForUprn(uprn: String)
                         (implicit session: ClientSideSession): Future[Option[AddressViewModel]]
}