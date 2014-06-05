package services.address_lookup.gds

import play.api.libs.ws.{Response, WS}
import scala.concurrent.Future
import play.api.Logger
import utils.helpers.Config
import services.address_lookup.AddressLookupWebService
import com.google.inject.Inject
import common.ClientSideSession

final class WebServiceImpl @Inject()(config: Config) extends AddressLookupWebService {
  private val baseUrl: String = config.gdsAddressLookupBaseUrl
  private val authorisation: String = config.gdsAddressLookupAuthorisation
  private val requestTimeout: Int = config.gdsAddressLookupRequestTimeout

  def postcodeWithNoSpaces(postcode: String): String = postcode.filter(_ != ' ')

  // request should look like    (GET, "/addresses?postcode=kt70ej").withHeaders(validAuthHeader)
  override def callPostcodeWebService(postcode: String)
                                     (implicit session: ClientSideSession): Future[Response] = {
    val endPoint = s"$baseUrl/addresses?postcode=${ postcodeWithNoSpaces(postcode) }"
    Logger.debug(s"Calling GDS postcode lookup service on $endPoint...")
    WS.url(endPoint).
      withHeaders("AUTHORIZATION" -> authorisation).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      get()
  }

  override def callUprnWebService(uprn: String)
                                 (implicit session: ClientSideSession): Future[Response] = {
    val endPoint = s"$baseUrl/uprn?uprn=$uprn"
    Logger.debug(s"Calling GDS uprn lookup service on $endPoint...")
    WS.url(endPoint).
      withHeaders("AUTHORIZATION" -> authorisation).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      get()
  }
}
