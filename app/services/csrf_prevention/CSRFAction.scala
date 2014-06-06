package services.csrf_prevention

import play.api.mvc._
import play.api.http.HeaderNames._
import services.csrf_prevention.CSRF._
import play.api.libs.iteratee._
import play.api.mvc.BodyParsers.parse._
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import scala.concurrent.Future
import play.api.libs.Crypto
import play.api.Logger

class CSRFAction(next: EssentialAction) extends EssentialAction {

  import CSRFAction._

  // An iteratee that returns a forbidden result saying the CSRF check failed
  private def checkFailed(req: RequestHeader, msg: String): Iteratee[Array[Byte], SimpleResult] = Done(errorHandler.handle(req, msg))

  def apply(request: RequestHeader) = {

    // this function exists purely to aid readability
    def continue = next(request)

    // Only filter unsafe methods and content types
    if (unsafeMethods(request.method) && request.contentType.exists(unsafeContentTypes)) {

      if (checkCsrfBypass(request)) {
        continue
      } else {

        val headerToken = "cookieName"  // TODO lookup tracking-id session/cookie

        // Only proceed with checks if there is an incoming token in the header, otherwise there's no point
        request.contentType match {
              case Some("application/x-www-form-urlencoded") => checkFormBody(request, headerToken, tokenName, next)
              case Some("multipart/form-data") => checkMultipartBody(request, headerToken, tokenName, next)
              // No way to extract token from text plain body
              case Some("text/plain") => {
                Logger.trace("[CSRF] Check failed because text/plain request")
                checkFailed(request, "No CSRF token found for text/plain body")
              }
            }


      }
    } else if (getTokenFromHeader(request, tokenName).isEmpty &&
      request.method == "GET" &&
      (request.accepts("text/html") || request.accepts("application/xml+xhtml"))) {

      // No token in header and we have to create one if not found, so create a new token
      val newToken = tokenProvider.generateToken

      // The request
      val requestWithNewToken = request.copy(tags = request.tags + (Token.RequestTag -> newToken))

      // Once done, add it to the result
      next(requestWithNewToken).map(result =>
        CSRFAction.addTokenToResponse(tokenName, newToken, request, result))

    } else {
      Logger.trace("[CSRF] No check necessary")
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
              } yield tokenProvider.compareTokens(token, tokenFromHeader)).getOrElse(false)
            )

            if (validToken) {
              // Feed the buffered bytes into the next request, and return the iteratee
              Logger.trace("[CSRF] Valid token found in body")
              Iteratee.flatten(Enumerator(bytes) |>> next(request))
            } else {
              Logger.trace("[CSRF] Check failed because no or invalid token found in body")
              checkFailed(request, "Invalid token found in form body")
            }
        })
    }
  }

}

object CSRFAction {

  def tokenName: String = "csrfToken"
  def unsafeMethods = Set("POST")
  def unsafeContentTypes = Set("application/x-www-form-urlencoded", "text/plain", "multipart/form-data")
  def errorHandler = CSRF.DefaultErrorHandler
  def tokenProvider = CSRF.SignedTokenProvider
  def postBodyBuffer: Long = 102400L

  private[csrf_prevention] def getTokenFromHeader(request: RequestHeader, tokenName: String) = {
    request.session.get("csrfToken")
  }

  private[csrf_prevention] def checkCsrfBypass(request: RequestHeader) = {
    if (request.headers.get("X-Requested-With").isDefined) {

      // AJAX requests are not CSRF attacks either because they are restricted to same origin policy
      Logger.trace("[CSRF] Bypassing check because X-Requested-With header found")
      true
    } else {
      false
    }
  }

  private[csrf_prevention] def addTokenToResponse(tokenName: String,
                                                  newToken: String, request: RequestHeader, result: SimpleResult) = {
    Logger.trace("[CSRF] Adding token to result: " + result)

    result.withSession(Session.deserialize(request.session.data))

  }
}

/**
 * CSRF check action.
 *
 * Apply this to all actions that require a CSRF check.
 */
object CSRFCheck {

  private class CSRFCheckAction[A](tokenName: String, tokenProvider: TokenProvider,
                                   errorHandler: ErrorHandler, wrapped: Action[A]) extends Action[A] {
    def parser = wrapped.parser

    def apply(request: Request[A]) = {

      // Maybe bypass
      if (CSRFAction.checkCsrfBypass(request) || !request.contentType.exists(CSRFAction.unsafeContentTypes)) {
        wrapped(request)
      } else {
        // Get token from header
        CSRFAction.getTokenFromHeader(request, tokenName).flatMap {
          headerToken =>

            val form = request.body match {
              case body: play.api.mvc.AnyContent if body.asFormUrlEncoded.isDefined => body.asFormUrlEncoded.get
              case body: play.api.mvc.AnyContent if body.asMultipartFormData.isDefined => body.asMultipartFormData.get.asFormUrlEncoded
              case body: Map[_, _] => body.asInstanceOf[Map[String, Seq[String]]]
              case body: play.api.mvc.MultipartFormData[_] => body.asFormUrlEncoded
              case _ => Map.empty[String, Seq[String]]
            }
            form.get(tokenName).flatMap(_.headOption)

              // Execute if it matches
              .collect {
              case queryToken if tokenProvider.compareTokens(queryToken, headerToken) => wrapped(request)
            }
        }.getOrElse(Future.successful(errorHandler.handle(request, "CSRF token check failed")))
      }
    }
  }

  /**
   * Wrap an action in a CSRF check.
   */
  def apply[A](action: Action[A], errorHandler: ErrorHandler = CSRFAction.errorHandler): Action[A] =
    new CSRFCheckAction(CSRFAction.tokenName, CSRFAction.tokenProvider, errorHandler, action)
}

/**
 * CSRF add token action.
 *
 * Apply this to all actions that render a form that contains a CSRF token.
 */
object CSRFAddToken {

  private class CSRFAddTokenAction[A](tokenName: String,
                                      tokenProvider: TokenProvider, wrapped: Action[A]) extends Action[A] {
    def parser = wrapped.parser

    def apply(request: Request[A]) = {
      if (CSRFAction.getTokenFromHeader(request, tokenName).isEmpty) {
        // No token in header and we have to create one if not found, so create a new token
        val newToken = tokenProvider.generateToken

        // The request
        val requestWithNewToken = new WrappedRequest(request) {
          override val tags = request.tags + (Token.RequestTag -> newToken)
        }

        // Once done, add it to the result
        wrapped(requestWithNewToken).map(result =>
          CSRFAction.addTokenToResponse(tokenName, newToken, request, result))
      } else {
        wrapped(request)
      }
    }
  }

  /**
   * Wrap an action in an action that ensures there is a CSRF token.
   */
  def apply[A](action: Action[A]): Action[A] =
    new CSRFAddTokenAction(CSRFAction.tokenName, CSRFAction.tokenProvider, action)
}