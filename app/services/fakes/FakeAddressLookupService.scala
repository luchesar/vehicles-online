package services.fakes

import models.domain.disposal_of_vehicle.AddressViewModel
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import javax.inject.Inject
import services.address_lookup.{AddressLookupWebService, AddressLookupService}
import FakeWebServiceImpl.{traderUprnValid, traderUprnValid2}

object FakeAddressLookupService {
  val postcodeInvalid = "xx99xx"
  val address1 = AddressViewModel(uprn = Some(traderUprnValid), address = Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  val address2 = AddressViewModel(uprn = Some(traderUprnValid2), address = Seq("Penarth Road", "Cardiff", "CF11 8TT"))
  val fetchedAddresses = Seq(
    address1.uprn.getOrElse(traderUprnValid).toString -> address1.address.mkString(", "),
    address2.uprn.getOrElse(traderUprnValid2).toString -> address2.address.mkString(", ")
  )
}
