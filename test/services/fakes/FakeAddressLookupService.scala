package services.fakes

import models.domain.disposal_of_vehicle.AddressViewModel
import FakeAddressLookupWebServiceImpl.{traderUprnValid, traderUprnValid2}

object FakeAddressLookupService {
  final val traderBusinessNameValid = "example trader name"
  final val postcodeInvalid = "xx99xx"
  val addressWithoutUprn = AddressViewModel(address= Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  val addressWithUprn = AddressViewModel(uprn = Some(traderUprnValid), address = Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  final val line1Valid = "1234"
  final val line2Valid = "line2 stub"
  final val line3Valid = "line3 stub"
  final val line4Valid = "line4 stub"
  final val postcodeValid = "CM81QJ"
  final val dateValid = "1970-11-25T00:00:00.000+01:00"
  final val postcodeValidWithSpace = "CM8 1QJ"
  final val postcodeNoResults = "SA99 1DD"
  val fetchedAddresses = Seq(
    traderUprnValid.toString -> addressWithUprn.address.mkString(", "),
    traderUprnValid2.toString -> addressWithUprn.address.mkString(", ")
  )
}
