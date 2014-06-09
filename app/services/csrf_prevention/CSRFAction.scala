package services.csrf_prevention

import play.api.mvc._
import play.api.http.HeaderNames._
import play.api.libs.iteratee._
import play.api.mvc.BodyParsers.parse._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
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

      // Only filter unsafe methods and content types
      if (unsafeMethods(request.method) && request.contentType.exists(unsafeContentTypes)) {

        if (checkCsrfBypass(request)) {
          next(request)
        } else {

          val headerToken = clientSideSessionFactory.getSession(request.cookies).trackingId // request.cookies.trackingId

          // Only proceed with checks if there is an incoming token in the header, otherwise there's no point
          request.contentType match {
            case Some("application/x-www-form-urlencoded") => checkFormBody(request, headerToken, tokenName, next)
            case Some("multipart/form-data") => checkMultipartBody(request, headerToken, tokenName, next)
            // No way to extract token from text plain body
            case _ => {
              Logger.trace("[CSRF] Check failed because request content type is not application/x-www-form-urlencoded or multipart/form-data")
              throw new CSRFException(new Throwable("No CSRF token found in body"))
            }
          }
        }
      } else if (request.method == "GET" &&
        (request.accepts("text/html") || request.accepts("application/xml+xhtml"))) {

        // No token in header and we have to create one if not found, so create a new token
        val newToken = Crypto.signToken(aesEncryption.encrypt("cookieName"))

        // The request
        val requestWithNewToken = request.copy(tags = request.tags + (Token.RequestTag -> newToken))

        // Once done, add it to the result
        next(requestWithNewToken)

      } else {
        Logger.trace("[CSRF] No check necessary")
        next(request)
      }
    } else {
      next(request)
    }
  }

  private def checkFormBody = checkBody[Map[String, Seq[String]]](tolerantFormUrlEncoded, identity) _

  private def checkMultipartBody = checkBody[MultipartFormData[Unit]](multipartFormData[Unit]({
    case _ => Iteratee.ignore[Array[Byte]].map(_ => MultipartFormData.FilePart("", "", None, ()))
  }), _.dataParts) _

  private def checkBody[T](parser: BodyParser[T], extractor: (T => Map[String, Seq[String]]))(request: RequestHeader, tokenFromHeader: String, tokenName: String, next: EssentialAction) = {
    // Take up to 100kb of the body
    val firstPartOfBody: Iteratee[Array[Byte], Array[Byte]] =
      Traversable.take[Array[Byte]](postBodyBuffer.asInstanceOf[Int]) &>> Iteratee.consume[Array[Byte]]()

    firstPartOfBody.flatMap {
      bytes: Array[Byte] =>
      // Parse the first 100kb
        val parsedBody = Enumerator(bytes) |>>> parser(request)

        Iteratee.flatten(parsedBody.map {
          parseResult =>
            val validToken = parseResult.fold(
              // error parsing the body, we couldn't find a valid token
              _ => false,
              // extract the token and verify
              body => (for {
                values <- extractor(body).get(tokenName)
                token <- values.headOption
              } yield (aesEncryption.decrypt(Crypto.extractSignedToken(token).get) == tokenFromHeader)).getOrElse(false)
            )

            if (validToken) {
              // Feed the buffered bytes into the next request, and return the iteratee
              Logger.trace("[CSRF] Valid token found in body")
              Iteratee.flatten(Enumerator(bytes) |>> next(request))
            } else {
              Logger.trace("[CSRF] Check failed because no or invalid token found in body")
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

  def unsafeMethods = Set("POST")

  def unsafeContentTypes = Set("application/x-www-form-urlencoded", "text/plain", "multipart/form-data")

  def postBodyBuffer: Long = 102400L

  def csrfPrevention = getProperty("csrf.prevention", default = true)

  private[csrf_prevention] def checkCsrfBypass(request: RequestHeader) = {

    if (request.headers.get("X-Requested-With").isDefined) {

      // AJAX requests are not CSRF attacks either because they are restricted to same origin policy
      Logger.trace("[CSRF] Bypassing check because X-Requested-With header found")
      true
    } else {
      false
    }
  }

  case class Token(value: String)

  object Token {

    val RequestTag = "CSRF_TOKEN"

    implicit def getToken(implicit request: RequestHeader): Token = {
      CSRFAction.getToken(request).getOrElse(sys.error("Missing CSRF Token"))
    }
  }

  def getToken(request: RequestHeader): Option[Token] = {
    Some(Token(Crypto.signToken(aesEncryption.encrypt(clientSideSessionFactory.getSession(request.cookies).trackingId))))
  }

}
