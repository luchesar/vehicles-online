package services.address_lookup

import scala.concurrent.Future
import play.api.libs.ws.Response

// Wrapper around our webservice call so that we can IoC fake versions for testing or use the real version.
trait AddressLookupWebService {
  def callPostcodeWebService(postcode: String): Future[Response]
  def callUprnWebService(uprn: String): Future[Response]
}
