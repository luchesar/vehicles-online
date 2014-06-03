package services.address_lookup

import models.domain.disposal_of_vehicle.{PostcodeToAddressResponse, AddressViewModel}
import scala.concurrent.Future
import play.api.libs.ws.Response
import common.ClientSideSession

trait AddressLookupService {
  def fetchAddressesForPostcode(postcode: String)
                               (implicit session: Option[ClientSideSession]): Future[Seq[(String, String)]]

  def fetchAddressForUprn(uprn: String)
                         (implicit session: Option[ClientSideSession]): Future[Option[AddressViewModel]]
}