package services.address_lookup.ordnance_survey

import play.api.libs.ws.{Response, WS}
import utils.helpers.Config
import scala.concurrent.Future
import play.api.Logger
import services.address_lookup.AddressLookupWebService
import com.google.inject.Inject
import common.{ClientSideSession, ClientSideSessionFactory}

final class WebServiceImpl @Inject()(config: Config) extends AddressLookupWebService {

  private val baseUrl: String = config.ordnanceSurveyMicroServiceUrl // TODO would it be better to move these to a companion object? And maybe private[this]
  private val requestTimeout: Int = config.ordnanceSurveyRequestTimeout

  def postcodeWithNoSpaces(postcode: String): String = postcode.filter(_ != ' ')

  override def callPostcodeWebService(postcode: String)
                                     (implicit session: Option[ClientSideSession]): Future[Response] = {

    val endPoint = s"$baseUrl/postcode-to-address?postcode=${postcodeWithNoSpaces(postcode)}${trackingIdParam(session)}"
    //Logger.debug(s"Calling ordnance-survey postcode lookup micro-service on $endPoint...")
    WS.url(endPoint).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      get()
  }

  override def callUprnWebService(uprn: String)
                                 (implicit session: Option[ClientSideSession]): Future[Response] = {
    val endPoint = s"$baseUrl/uprn-to-address?uprn=${uprn}${trackingIdParam(session)}"
    //Logger.debug(s"Calling ordnance-survey uprn lookup micro-service on $endPoint...")
    WS.url(endPoint).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      get()
  }

  private def trackingIdParam(session: Option[ClientSideSession]): String = session match {
    case Some(s) => s"&${ClientSideSessionFactory.SessionIdCookieName}=${s.trackingId}"
    case _ => ""
  }

}
