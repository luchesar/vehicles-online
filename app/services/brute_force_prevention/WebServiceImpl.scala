package services.brute_force_prevention

import play.api.Logger
import play.api.libs.ws.{Response, WS}
import scala.concurrent.Future
import utils.helpers.Config

final class WebServiceImpl extends BruteForcePreventionWebService {
  private val baseUrl: String = Config.bruteForcePreventionMicroServiceBaseUrl
  private val requestTimeout: Int = Config.bruteForcePreventionTimeout

  override def callBruteForce(vrm: String): Future[Response] = {
    val endPoint = s"$baseUrl/security"
    Logger.debug(s"Calling brute force prevention on $endPoint with vrm: $vrm")
    WS.url(endPoint).
      withRequestTimeout(requestTimeout). // Timeout is in milliseconds
      post(Map("tokenList" -> Seq(vrm)))
  }
}
