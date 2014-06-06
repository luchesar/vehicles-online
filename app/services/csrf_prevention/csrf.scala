package services.csrf_prevention

import play.api.mvc._
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

}

