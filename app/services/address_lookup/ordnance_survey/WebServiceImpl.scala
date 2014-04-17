package services.address_lookup.ordnance_survey

import play.api.libs.ws.{Response, WS}
import utils.helpers.Config
import scala.concurrent.Future
import play.api.Logger
import services.address_lookup.AddressLookupWebService
import mappings.common.Postcode.postcodeId

class WebServiceImpl extends AddressLookupWebService {
  val baseUrl = s"${ Config.ordnanceSurveyBaseUrl }"
  val requestTimeout = Config.ordnanceSurveyRequestTimeout.toInt

  def postcodeWithNoSpaces(postcode: String): String = postcode.filter(_ != ' ')

  override def callPostcodeWebService(postcode: String): Future[Response] = {
    val endPoint = s"$baseUrl/postcode-to-address"
    Logger.debug(s"Calling Ordnance Survey postcode lookup micro-service on $endPoint...")
    WS.url(endPoint).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      post(Map(postcodeId -> Seq(postcodeWithNoSpaces(postcode))))
  }

  override def callUprnWebService(uprn: String): Future[Response] = {
    val endPoint = s"$baseUrl/uprn?uprn=$uprn" // TODO change URL
    Logger.debug(s"Calling Ordnance Survey uprn lookup micro-service on $endPoint...")
    WS.url(endPoint).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      get()
  }
}
