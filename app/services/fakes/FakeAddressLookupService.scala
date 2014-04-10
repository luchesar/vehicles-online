package services.fakes

import models.domain.disposal_of_vehicle.AddressViewModel
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import javax.inject.Inject
import services.address_lookup.{AddressLookupWebService, AddressLookupService}
import FakeWebServiceImpl.{traderUprnValid, traderUprnValid2}

object FakeAddressLookupService {
  val postcodeInvalid = "xx99xx"
  val addressWithoutUprn = AddressViewModel(address= Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  val addressWithUprn = AddressViewModel(uprn = Some(traderUprnValid), address = Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  val line1Valid = "123"
  val line2Valid = "line2 stub"
  val line3Valid = "line3 stub"
  val line4Valid = "line4 stub"
  val postcodeValid = "CM81QJ"
  val postcodeValidWithSpace = "CM8 1QJ"
  val postcodeNoResults = "SA99 1DD"
  val fetchedAddresses = Seq(
    traderUprnValid.toString -> addressWithUprn.address.mkString(", "),
    traderUprnValid2.toString -> addressWithUprn.address.mkString(", ")
  )
}
