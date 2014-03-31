package services.address_lookup.ordnance_survey

import play.api.libs.ws.{Response, WS}
import utils.helpers.Config
import scala.concurrent.Future
import play.api.Logger
import com.ning.http.client.Realm.AuthScheme
import services.address_lookup.AddressLookupWebService

class WebServiceImpl extends AddressLookupWebService {
  val username = s"${ Config.ordnanceSurveyUsername }"
  val password = s"${ Config.ordnanceSurveyPassword }"
  val baseUrl = s"${ Config.ordnanceSurveyBaseUrl }"
  val requestTimeout = Config.ordnanceSurveyRequestTimeout.toInt

  def postcodeWithNoSpaces(postcode: String): String = postcode.filter(_ != ' ')

  override def callPostcodeWebService(postcode: String): Future[Response] = {
    val endPoint = s"$baseUrl/postcode?postcode=${ postcodeWithNoSpaces(postcode) }&dataset=dpa" // TODO add lpi to URL, but need to set organisation as Option on the type.
    Logger.debug(s"Calling Ordnance Survey postcode lookup service on $endPoint...")
    WS.url(endPoint).
      withAuth(username = username, password = password, scheme = AuthScheme.BASIC).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      get()
  }

  override def callUprnWebService(uprn: String): Future[Response] = {
    val endPoint = s"$baseUrl/uprn?uprn=$uprn&dataset=dpa" // TODO add lpi to URL, but need to set orgnaisation as Option on the type.
    Logger.debug(s"Calling Ordnance Survey uprn lookup service on $endPoint...")
    WS.url(endPoint).
      withAuth(username = username, password = password, scheme = AuthScheme.BASIC).
      withRequestTimeout(30000). // Timeout is in milliseconds
      get()
  }
}
