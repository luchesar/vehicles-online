package services.address_lookup.ordnance_survey

import play.api.libs.ws.{Response, WS}
import utils.helpers.Config
import scala.concurrent.Future
import play.api.Logger
import services.address_lookup.AddressLookupWebService
import mappings.common.Postcode.PostcodeId
import mappings.common.Uprn.UprnId
import play.api.libs.json.Json

final class WebServiceImpl extends AddressLookupWebService {
  private val baseUrl: String = Config.ordnanceSurveyMicroServiceUrl
  private val requestTimeout: Int = Config.ordnanceSurveyRequestTimeout.toInt

  def postcodeWithNoSpaces(postcode: String): String = postcode.filter(_ != ' ')

  override def callPostcodeWebService(postcode: String): Future[Response] = {
    val endPoint = s"$baseUrl/postcode-to-address?postcode=${postcodeWithNoSpaces(postcode)}"
    Logger.debug(s"Calling ordnance-survey postcode lookup micro-service on $endPoint...")
    WS.url(endPoint).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      get()
  }

  override def callUprnWebService(uprn: String): Future[Response] = {
    val endPoint = s"$baseUrl/uprn-to-address?uprn=${uprn}"
    Logger.debug(s"Calling ordnance-survey uprn lookup micro-service on $endPoint...")
    WS.url(endPoint).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      get()
  }

}
