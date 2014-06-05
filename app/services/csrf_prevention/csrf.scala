package services.csrf_prevention

import play.api.mvc._
import play.api._
import play.api.mvc.Results._
import play.api.libs.Crypto

private[csrf_prevention] object CSRFConf {

  import play.api.Play.current

  def c = Play.configuration

  def TokenName: String = "csrfToken"

  def PostBodyBuffer: Long = c.getBytes("csrf.body.bufferSize").getOrElse(102400L)

  val UnsafeMethods = Set("POST")
  val UnsafeContentTypes = Set("application/x-www-form-urlencoded", "text/plain", "multipart/form-data")

  val HeaderName = "Csrf-Token"
  val HeaderNoCheck = "nocheck"

  def defaultCreateIfNotFound(request: RequestHeader) = {
    // If the request isn't accepting HTML, then it won't be rendering a form, so there's no point in generating a
    // CSRF token for it.
    request.method == "GET" && (request.accepts("text/html") || request.accepts("application/xml+xhtml"))
  }

  def defaultErrorHandler = CSRF.DefaultErrorHandler

  def defaultTokenProvider = CSRF.SignedTokenProvider

}

object CSRF {

  private[csrf_prevention] val filterLogger = play.api.Logger("play.filters")

  /**
   * A CSRF token
   */
  case class Token(value: String)

  object Token {
    val RequestTag = "CSRF_TOKEN"

    implicit def getToken(implicit request: RequestHeader): Token = {
      CSRF.getToken(request).getOrElse(sys.error("Missing CSRF Token"))
    }
  }

  // Allows the template helper to access it
  def TokenName = CSRFConf.TokenName

  import CSRFConf._

  /**
   * Extract token from current request
   */
  def getToken(request: RequestHeader): Option[Token] = {
    Some(Token(Crypto.signToken("1234567890"))) // TODO lookup tracking-id session/cookie
  }

  /**
   * A token provider, for generating and comparing tokens.
   *
   * This abstraction allows the use of randomised tokens.
   */
  trait TokenProvider {
    /** Generate a token */
    def generateToken: String

    /** Compare two tokens */
    def compareTokens(tokenA: String, tokenB: String): Boolean
  }

  object SignedTokenProvider extends TokenProvider {
    def generateToken = {
      Crypto.signToken("1234567890") // TODO lookup tracking-id session/cookie
    }

    def compareTokens(tokenA: String, tokenB: String) = {
      Crypto.compareSignedTokens(tokenA, tokenB)
    }
  }

  /**
   * This trait handles the CSRF error.
   */
  trait ErrorHandler {
    /** Handle a result */
    def handle(req: RequestHeader, msg: String): SimpleResult
  }

  object DefaultErrorHandler extends ErrorHandler {
    def handle(req: RequestHeader, msg: String) = Forbidden(msg)
  }

}

