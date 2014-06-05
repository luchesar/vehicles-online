package services.csrf_prevention

import play.api.mvc._
import services.csrf_prevention.CSRF.{TokenProvider, ErrorHandler}
import play.api.Play

class CSRFFilter(tokenName: => String = CSRFConf.TokenName,
                 createIfNotFound: (RequestHeader) => Boolean = CSRFConf.defaultCreateIfNotFound,
                 tokenProvider: => TokenProvider = CSRFConf.defaultTokenProvider,
                 errorHandler: => ErrorHandler = CSRFConf.defaultErrorHandler) extends EssentialFilter {

  def apply(next: EssentialAction): EssentialAction = new CSRFAction(next, tokenName,
    createIfNotFound, tokenProvider, errorHandler)
}

object CSRFFilter {
  def apply(tokenName: => String = CSRFConf.TokenName,
            createIfNotFound: (RequestHeader) => Boolean = CSRFConf.defaultCreateIfNotFound,
            tokenProvider: => TokenProvider = CSRFConf.defaultTokenProvider,
            errorHandler: => ErrorHandler = CSRFConf.defaultErrorHandler) = {
    new CSRFFilter(tokenName, createIfNotFound, tokenProvider, errorHandler)
  }

}