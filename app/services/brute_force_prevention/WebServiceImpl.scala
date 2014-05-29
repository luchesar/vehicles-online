package services.brute_force_prevention

import play.api.Logger
import play.api.libs.ws.{Response, WS}
import scala.concurrent.Future

final class WebServiceImpl extends BruteForcePreventionWebService {
  private val baseUrl: String = "http://localhost:9910/security" //Config.bruteForcePreventionMicroServiceBase // TODO get value from config
  private val requestTimeout: Int = 5000 //Config.ordnanceSurveyRequestTimeout.toInt

  override def callBruteForce(vrm: String): Future[Response] = {
    val endPoint = s"$baseUrl/security"
    Logger.debug(s"Calling brute force prevention on $baseUrl with vrm: $vrm")
    WS.url(endPoint).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      post(Map("tokenList" -> Seq(vrm)))
  }
}
