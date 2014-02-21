package services

import models.domain.disposal_of_vehicle.AddressViewModel

trait AddressLookupService {
  def fetchAddress(postcode: String): Map[String, String]
  def lookupAddress(address: String): AddressViewModel
}