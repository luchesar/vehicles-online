package services.ordnance_survey

import services.AddressLookupService
import models.domain.disposal_of_vehicle.AddressViewModel

class AddressLookupServiceImpl extends AddressLookupService {
  val address1 = AddressViewModel(uprn = Some(1234), address = Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  val address2 = AddressViewModel(uprn = Some(4567), address = Seq("Penarth Road", "Cardiff", "CF11 8TT"))

  override def fetchAddressesForPostcode(postcode: String): Map[String, String] = {
    Map(
      address1.uprn.getOrElse(1234).toString -> address1.address.mkString(", "),
      address2.uprn.getOrElse(4567).toString -> address2.address.mkString(", ")
    ) // TODO this should come from call to GDS lookup.
  }

  override def lookupAddress(uprn: String): AddressViewModel = {
    val addresses = Map(
      address1.uprn.getOrElse(1234).toString -> address1,
      address2.uprn.getOrElse(4567).toString -> address2
    ) // TODO this should come from call to GDS lookup.

    addresses(uprn)
  }
}
