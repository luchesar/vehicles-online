package services

import models.domain.disposal_of_vehicle.AddressViewModel
import scala.concurrent.Future
import play.api.libs.ws.Response
import services.address_lookup.ordnance_survey.domain.OSAddressbaseResult

trait AddressLookupService {
  protected def callPostcodeWebService(postcode: String): Future[Response]
  protected def callUprnWebService(postcode: String): Future[Response]
  def fetchAddressesForPostcode(postcode: String): Future[Seq[(String, String)]]
  def fetchAddressForUprn(uprn: String): Future[Option[AddressViewModel]]
}