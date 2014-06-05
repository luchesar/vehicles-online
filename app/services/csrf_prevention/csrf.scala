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
  def SignTokens: Boolean = true

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
    println(TokenName)
    val token = request.session.get(TokenName)
    for (key <- request.session.data) {
      println(key._1)
      println(request.session.data.get(key._1))
    }
    println("~~~~~~~~~~~~~~~~~~~~~~~~~")
    println(token)
    if (SignTokens) {
      // Extract the signed token, and then resign it. This makes the token random per request, preventing the BREACH
      // vulnerability
      token.flatMap(Crypto.extractSignedToken)
        .map(token => Token(Crypto.signToken(token)))
    } else {
      token.map(Token.apply)
    }
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
      val tok = Crypto.generateSignedToken
//      val tok = Crypto.signToken("1234567890") // TODO lookup tracking-id
      println(">>>>>>>>>>>>>>>>>>>>>>>>")
      println(tok)
      tok
    }
    def compareTokens(tokenA: String, tokenB: String) = {
      println("++++++++++++++++++++")
      println(tokenA)
      println(tokenB)
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

