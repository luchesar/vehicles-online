package services

import models.domain.disposal_of_vehicle.AddressViewModel
import scala.concurrent.{Future}

trait AddressLookupService {
  def fetchAddressesForPostcode(postcode: String): Future[Map[String, String]]
  def lookupAddress(uprn: String): AddressViewModel
}