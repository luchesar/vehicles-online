package services.address_lookup.ordnance_survey

import play.api.libs.ws.{Response, WS}
import utils.helpers.Config
import scala.concurrent.Future
import play.api.Logger
import services.address_lookup.AddressLookupWebService
import mappings.common.Postcode.postcodeId
import mappings.common.Uprn.uprnId
import play.api.libs.json.Json

class WebServiceImpl extends AddressLookupWebService {
  val baseUrl = s"${ Config.ordnanceSurveyBaseUrl }"
  val requestTimeout = Config.ordnanceSurveyRequestTimeout.toInt

  def postcodeWithNoSpaces(postcode: String): String = postcode.filter(_ != ' ')

  override def callPostcodeWebService(postcode: String): Future[Response] = {
    val endPoint = s"$baseUrl/postcode-to-address"
    Logger.debug(s"Calling ordnance-survey postcode lookup micro-service on $endPoint...")
    val dataAsJson = Json.obj(postcodeId -> postcode)
    WS.url(endPoint).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      post(dataAsJson)

  }

  override def callUprnWebService(uprn: String): Future[Response] = {
    val endPoint = s"$baseUrl/uprn-to-address"
    Logger.debug(s"Calling ordnance-survey uprn lookup micro-service on $endPoint...")
    val dataAsJson = Json.obj(uprnId -> Some(uprn.toLong))
    WS.url(endPoint).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      post(dataAsJson)
  }
}
