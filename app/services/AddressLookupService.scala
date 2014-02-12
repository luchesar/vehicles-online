package services

import models.domain.common.Address

trait AddressLookupService {
  def fetchAddress(postcode: String): Map[String, String]
  def lookupAddress(address: String): Address
}