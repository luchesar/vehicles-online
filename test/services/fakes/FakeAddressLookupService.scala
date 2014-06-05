package services.fakes

import FakeAddressLookupWebServiceImpl.{traderUprnValid, traderUprnValid2}
import models.domain.disposal_of_vehicle.AddressViewModel

object FakeAddressLookupService {
  final val TraderBusinessNameValid = "example trader name"
  final val PostcodeInvalid = "xx99xx"
  val addressWithoutUprn = AddressViewModel(address = Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  val addressWithUprn = AddressViewModel(uprn = Some(traderUprnValid), address = Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  final val BuildingNameOrNumberValid = "1234"
  final val Line2Valid = "line2 stub"
  final val Line3Valid = "line3 stub"
  final val PostTownValid = "postTown stub"
  final val PostcodeValid = "CM81QJ"

  final val PostcodeValidWithSpace = "CM8 1QJ"
  final val PostcodeNoResults = "SA99 1DD"
  val fetchedAddresses = Seq(
    traderUprnValid.toString -> addressWithUprn.address.mkString(", "),
    traderUprnValid2.toString -> addressWithUprn.address.mkString(", ")
  )
}