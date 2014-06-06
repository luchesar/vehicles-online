package services.csrf_prevention

import play.api.mvc._
import play.api._
import play.api.mvc.Results._
import play.api.libs.Crypto
import utils.helpers.AesEncryption

object CSRF {

  def aesEncryption = new AesEncryption()

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

  /**
   * Extract token from current request
   */
  def getToken(request: RequestHeader): Option[Token] = {
    Some(Token(Crypto.signToken(aesEncryption.encrypt("cookieName")))) // TODO lookup tracking-id session/cookie
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
      Crypto.signToken(aesEncryption.encrypt("cookieName")) // TODO lookup tracking-id session/cookie
    }
    def compareTokens(tokenA: String, tokenB: String) = {
      aesEncryption.decrypt(Crypto.extractSignedToken(tokenA).get) == tokenB
    }
  }

}

