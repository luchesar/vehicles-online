package services.address_lookup.ordnance_survey

import play.api.libs.ws.{Response, WS}
import services.HttpHeaders
import utils.helpers.Config
import scala.concurrent.Future
import services.address_lookup.AddressLookupWebService
import com.google.inject.Inject
import common.{LogFormats, ClientSideSessionFactory}
import play.api.Logger
import play.api.i18n.Lang

final class WebServiceImpl @Inject()(config: Config) extends AddressLookupWebService {
  private val baseUrl: String = config.ordnanceSurveyMicroServiceUrl // TODO would it be better to move these to a companion object? And maybe private[this]
  private val requestTimeout: Int = config.ordnanceSurveyRequestTimeout

  override def callPostcodeWebService(postcode: String, trackingId: String)
                                     (implicit lang: Lang): Future[Response] = {
    val endPoint = s"$baseUrl/postcode-to-address?" +
      postcodeParam(postcode) +
      languageParam +
      trackingIdParam(trackingId)

    val postcodeToLog = LogFormats.anonymize(postcode)

    Logger.debug(s"Calling ordnance-survey postcode lookup micro-service with $postcodeToLog") // $endPoint...")
    WS.url(endPoint).
      withHeaders(HttpHeaders.TrackingId -> trackingId).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      get()
  }

  override def callUprnWebService(uprn: String, trackingId: String)
                                 (implicit lang: Lang): Future[Response] = {
    val endPoint = s"$baseUrl/uprn-to-address?" +
      s"uprn=$uprn" +
      languageParam +
      trackingIdParam(trackingId)

    val uprnToLog = LogFormats.anonymize(uprn)

    Logger.debug(s"Calling ordnance-survey uprn lookup micro-service with $uprnToLog")
    WS.url(endPoint).
      withHeaders(HttpHeaders.TrackingId -> trackingId).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      get()
  }

  def postcodeWithNoSpaces(postcode: String): String = postcode.filter(_ != ' ')

  private def postcodeParam(postcode: String) = s"postcode=${postcodeWithNoSpaces(postcode)}"

  private def trackingIdParam(trackingId: String): String =
    s"&${ClientSideSessionFactory.TrackingIdCookieName}=$trackingId"

  private def languageParam(implicit lang: Lang) = s"&languageCode=${lang.code.toUpperCase}"
}