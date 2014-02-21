package services

import models.domain.disposal_of_vehicle.AddressViewModel

trait AddressLookupService {
  def fetchAddressesForPostcode(postcode: String): Map[String, String]
  def lookupAddress(uprn: String): AddressViewModel
}