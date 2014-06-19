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

final case class CSRFException(nestedException: Throwable) extends Exception(nestedException: Throwable)

class CSRFAction(next: EssentialAction) extends EssentialAction {

  import CSRFAction._

  def apply(request: RequestHeader) = {

    // check if csrf prevention is switched on
    if (csrfPrevention) {

      if (request.method == "POST") {

        val headerToken =
          buildTokenWithReferer(clientSideSessionFactory.getSession(request.cookies).trackingId, request.headers)

        if (request.contentType.get == "application/x-www-form-urlencoded") {
          checkBody(request, headerToken, next)
        } else {
          throw new CSRFException(new Throwable("No CSRF token found in body"))
        }

      } else if (request.method == "GET" && request.accepts("text/html")) {

        // No token in header and we have to create one if not found, so create a new token
        val newToken = buildTokenWithUri(clientSideSessionFactory.getSession(request.cookies).trackingId, request.uri)

        val newEncryptedToken = aesEncryption.encrypt(newToken)

        val newSignedEncryptedToken = Crypto.signToken(newEncryptedToken)

        val requestWithNewToken = request.copy(tags = request.tags + ("CSRF_TOKEN" -> newSignedEncryptedToken))

        // Once done, add it to the result
        next(requestWithNewToken)

      } else {
        next(request)
      }
    } else {
      next(request)
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
                values <- identity(body).get(tokenName)
                token <- values.headOption
              } yield {
                val decryptedExtractedSignedToken = aesEncryption.decrypt(Crypto.extractSignedToken(token).getOrElse(
                  throw new CSRFException(new Throwable("Invalid token found in form body"))))
                val splitDecryptedExtractedSignedToken = splitToken(decryptedExtractedSignedToken)
                val splitTokenFromHeader = splitToken(headerToken)
                ((splitDecryptedExtractedSignedToken._1 == splitTokenFromHeader._1) &&
                  (splitTokenFromHeader._2.contains(splitDecryptedExtractedSignedToken._2)))
              }).getOrElse(false)
            )

            if (validToken) {
              Iteratee.flatten(Enumerator(bytes) |>> next(request))
            } else {
              throw new CSRFException(new Throwable("Invalid token found in form body"))
            }
        })
    }

  }

}

object CSRFAction {

  implicit val clientSideSessionFactory = Composition.devInjector.getInstance(classOf[ClientSideSessionFactory])

  def aesEncryption = new AesEncryption()

  def tokenName: String = "csrfToken"

  def csrfPrevention = getProperty("csrf.prevention", default = true)

  case class Token(value: String)

  object Token {

    val Delimiter = "-"

    implicit def getToken(implicit request: RequestHeader): Token = {
      CSRFAction.getToken(request).getOrElse(sys.error("Missing CSRF Token"))
    }
  }

  def getToken(request: RequestHeader): Option[Token] = {
    Some(Token(Crypto.signToken(aesEncryption.encrypt(
      buildTokenWithUri(clientSideSessionFactory.getSession(request.cookies).trackingId, request.uri)))))
  }

  def buildTokenWithReferer(trackingId: String, requestHeaders: Headers) = {
    trackingId + Token.Delimiter + requestHeaders.get("Referer")
  }

  def buildTokenWithUri(trackingId: String, uri: String) = {
    trackingId + Token.Delimiter + uri
  }

  def splitToken(token: String): (String, String) = {
    (token.split(Token.Delimiter)(0), token.drop(token.indexOf(Token.Delimiter) + 1))
  }

}
