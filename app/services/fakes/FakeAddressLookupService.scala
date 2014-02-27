package services.fakes

import models.domain.disposal_of_vehicle.AddressViewModel
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import play.api.libs.ws.Response
import javax.inject.Inject
import services.AddressLookupService
import services.ordnance_survey.domain.OSAddressbaseResult

/**
 * Fake implementation of the FakeAddressLookupService trait
 */
class FakeAddressLookupService @Inject()(ws: services.WebService) extends AddressLookupService {
  override protected def callPostcodeWebService(postcode: String): Future[Response] = ???

  override protected def callUprnWebService(postcode: String): Future[Response] = ???

  override protected def extractFromJson(resp: Response): Option[Seq[OSAddressbaseResult]] = ???

  override def fetchAddressesForPostcode(postcode: String): Future[Seq[(String, String)]] = Future {
    if (postcode == FakeAddressLookupService.postcodeInvalid) Seq.empty
    else FakeAddressLookupService.fetchedAddresses
  }

  override def fetchAddressForUprn(uprn: String): Future[Option[AddressViewModel]] = Future {
    Some(FakeAddressLookupService.address1)
  }
}

object FakeAddressLookupService {
  val postcodeInvalid = "xx99xx"
  val address1 = AddressViewModel(uprn = Some(1234L), address = Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  val address2 = AddressViewModel(uprn = Some(4567L), address = Seq("Penarth Road", "Cardiff", "CF11 8TT"))
  val fetchedAddresses = Seq(
    address1.uprn.getOrElse(1234L).toString -> address1.address.mkString(", "),
    address2.uprn.getOrElse(4567L).toString -> address2.address.mkString(", ")
  )
}
