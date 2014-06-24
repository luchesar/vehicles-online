package services.csrf_prevention

import play.api.mvc._
import play.api.libs.iteratee._
import play.api.mvc.BodyParsers.parse._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.libs.Crypto
import play.api.Logger
import app.ConfigProperties._
import common.ClientSideSessionFactory
import scala.Some
import utils.helpers.AesEncryption
import composition.Composition

final case class CSRFPreventionException(nestedException: Throwable) extends Exception(nestedException: Throwable)

/**
 * This class is based upon the Play's v2.2 CSRF protection. It has been stripped of code not relevant to this project, and
 * project specific exception handling and encryption has been added. The unmarshalling and onward streaming in the
 * checkBody method is as Play intended it apart from the token comparison.
 *
 * https://www.playframework.com/documentation/2.2.x/ScalaCsrf
 *
 */
class CSRFPreventionAction(next: EssentialAction) extends EssentialAction {

  import CSRFPreventionAction._

  def apply(requestHeader: RequestHeader) = {

    // check if csrf prevention is switched on
    if (csrfPrevention) {

      if (requestHeader.method == "POST") {

        val headerToken =
          buildTokenWithReferer(clientSideSessionFactory.getSession(requestHeader.cookies).trackingId, requestHeader.headers)

        if (requestHeader.contentType.get == "application/x-www-form-urlencoded") {
          checkBody(requestHeader, headerToken, next)
        } else {
          throw new CSRFPreventionException(new Throwable("No CSRF token found in body"))
        }

      } else if (requestHeader.method == "GET" && requestHeader.accepts("text/html")) {

        // No token in header and we have to create one if not found, so create a new token
        val newToken = buildTokenWithUri(clientSideSessionFactory.getSession(requestHeader.cookies).trackingId, requestHeader.uri)

        val newEncryptedToken = aesEncryption.encrypt(newToken)

        val newSignedEncryptedToken = Crypto.signToken(newEncryptedToken)

        val requestWithNewToken = requestHeader.copy(tags = requestHeader.tags + ("CSRF_PREVENTION_TOKEN" -> newSignedEncryptedToken))

        // Once done, add it to the result
        next(requestWithNewToken)

      } else {
        next(requestHeader)
      }
    } else {
      next(requestHeader)
    }
  }

  private def checkBody(request: RequestHeader, headerToken: String, next: EssentialAction) = {

    val firstPartOfBody: Iteratee[Array[Byte], Array[Byte]] =
      Traversable.take[Array[Byte]](102400L.asInstanceOf[Int]) &>> Iteratee.consume[Array[Byte]]()

    firstPartOfBody.flatMap {
      bytes: Array[Byte] =>

        val parsedBody = Enumerator(bytes) |>>> tolerantFormUrlEncoded(request)

        Iteratee.flatten(parsedBody.map {
          parseResult =>
            val validToken = parseResult.fold(
              // valid token not found
              _ => false,
              // valid token found
              body => (for {
                values <- identity(body).get(csrfPreventionTokenName)
                token <- values.headOption
              } yield {
                val decryptedExtractedSignedToken = aesEncryption.decrypt(Crypto.extractSignedToken(token).getOrElse(
                  throw new CSRFPreventionException(new Throwable("Invalid token found in form body"))))
                val splitDecryptedExtractedSignedToken = splitToken(decryptedExtractedSignedToken)
                val splitTokenFromHeader = splitToken(headerToken)
                ((splitDecryptedExtractedSignedToken._1 == splitTokenFromHeader._1) &&
                  (splitTokenFromHeader._2.contains(splitDecryptedExtractedSignedToken._2)))
              }).getOrElse(false)
            )

            if (validToken) {
              Iteratee.flatten(Enumerator(bytes) |>> next(request))
            } else {
              throw new CSRFPreventionException(new Throwable("Invalid token found in form body"))
            }
        })
    }

  }

}

object CSRFPreventionAction {

  implicit val clientSideSessionFactory = Composition.devInjector.getInstance(classOf[ClientSideSessionFactory])

  def aesEncryption = new AesEncryption()

  def csrfPreventionTokenName: String = "csrf_prevention_Token"

  def csrfPrevention = getProperty("csrf.prevention", default = true)

  case class CSRFPreventionToken(value: String)

  val Delimiter = "-"
  
  // TODO : Trap the missing token exception differently?
  implicit def getToken(implicit request: RequestHeader): CSRFPreventionToken = {
    Some(CSRFPreventionToken(Crypto.signToken(aesEncryption.encrypt(
      buildTokenWithUri(clientSideSessionFactory.getSession(request.cookies).trackingId, request.uri))))).getOrElse(throw new CSRFPreventionException(throw new Throwable("No CSRF token found")))
  }

  private def buildTokenWithReferer(trackingId: String, requestHeaders: Headers) = {
    trackingId + Delimiter + requestHeaders.get("Referer")
  }

  private def buildTokenWithUri(trackingId: String, uri: String) = {
    trackingId + Delimiter + uri
  }

  private def splitToken(token: String): (String, String) = {
    (token.split(Delimiter)(0), token.drop(token.indexOf(Delimiter) + 1))
  }

}
