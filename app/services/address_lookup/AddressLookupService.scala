package services.address_lookup

import models.domain.disposal_of_vehicle.AddressViewModel
import scala.concurrent.Future
import common.ClientSideSession
import play.api.i18n.Lang

trait AddressLookupService {
  // TODO we should not be passing ClientSideSession, we should just pass the tracking ID.
  def fetchAddressesForPostcode(postcode: String)
                               (implicit session: ClientSideSession, lang: Lang): Future[Seq[(String, String)]]

  def fetchAddressForUprn(uprn: String)
                         (implicit session: ClientSideSession, lang: Lang): Future[Option[AddressViewModel]]
}