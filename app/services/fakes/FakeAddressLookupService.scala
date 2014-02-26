package services.fakes

import services.AddressLookupService
import models.domain.disposal_of_vehicle.AddressViewModel
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global

/**
 * Fake implementation of the FakeAddressLookupService trait
 */
class FakeAddressLookupService() extends AddressLookupService {
  val address1 = AddressViewModel(uprn = Some(1234L), address = Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  val address2 = AddressViewModel(uprn = Some(4567L), address = Seq("Penarth Road", "Cardiff", "CF11 8TT"))

  override def fetchAddressesForPostcode(postcode: String): Future[Seq[(String, String)]] = Future {
    if (postcode == FakeAddressLookupService.postcodeInvalid) {
      Seq.empty
    }
    else {
      Seq(
        address1.uprn.getOrElse(1234L).toString -> address1.address.mkString(", "),
        address2.uprn.getOrElse(4567L).toString -> address2.address.mkString(", ")
      )
    }
  }

  override def fetchAddressForUprn(uprn: String): Future[Option[AddressViewModel]] = Future {
    Some(address1)
  }
}

object FakeAddressLookupService {
  val postcodeInvalid = "xx99xx"
}
