package composition

import com.google.inject.Guice
import play.api.mvc.{Session, RequestHeader, EssentialFilter}
import play.filters.csrf.CSRF._
import play.filters.csrf.CSRFFilter
import java.security.SecureRandom
import org.apache.commons.codec.binary.Hex
import play.api.libs.Crypto


object CustomSignedTokenProvider extends TokenProvider {

  def generateCryptoToken = {
    val bytes = new Array[Byte](12)
    new SecureRandom().nextBytes(bytes)
    new String(Hex.encodeHex(bytes))
  }

  def generateSignedToken = Crypto.signToken(generateCryptoToken)
  def generateToken = generateSignedToken
  def compareTokens(tokenA: String, tokenB: String) = {
    Crypto.compareSignedTokens(tokenA, tokenB)
  }

}


object Composition {
  /**
   * Application configuration is in a hierarchy of files:
   *
   * application.conf
   * /             |            \
   * application.prod.conf    application.dev.conf    application.test.conf <- these can override and add to application.conf
   *
   * play test  <- test mode picks up application.test.conf
   * play run   <- dev mode picks up application.dev.conf
   * play start <- prod mode picks up application.prod.conf
   *
   * To override and stipulate a particular "conf" e.g.
   * play -Dconfig.file=conf/application.test.conf run
   */
  lazy val devInjector = Guice.createInjector(DevModule)

  lazy val filters: EssentialFilter = new CSRFFilter(tokenProvider = CustomSignedTokenProvider)

}
