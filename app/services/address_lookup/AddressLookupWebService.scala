package services.address_lookup

import scala.concurrent.Future
import play.api.libs.ws.Response
import common.ClientSideSession

// Wrapper around our webservice call so that we can IoC fake versions for testing or use the real version.
trait AddressLookupWebService {

  def callPostcodeWebService(postcode: String)
                            (implicit session: ClientSideSession): Future[Response]

  def callUprnWebService(uprn: String)
                        (implicit session: ClientSideSession): Future[Response]
}
