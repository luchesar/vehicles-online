package common

import play.api.test.WithApplication
import utils.helpers._
import composition.TestComposition.{testInjector => injector}
import helpers.UnitSpec
import controllers.disposal_of_vehicle.SetUpTradeDetails
import common.CookieHelper._
import common.CookieImplicits.SimpleResultAdapter

final class EncryptedClientSideSessionFactorySpec extends UnitSpec {
  "newSession" should {
    "return result containing a new session secret cookie" in new WithApplication {
      val request = FakeCSRFRequest()
      val result = setUpTradeDetails.present(request)

      whenReady(result) {
        r =>
          val cookiesBefore = fetchCookiesFromHeaders(r)
          cookiesBefore.size should equal(0) // There should be no cookies in the result before we call the factory
          val encryptedClientSideSessionFactory = new EncryptedClientSideSessionFactory()(noCookieFlags, noEncryption, noHashing)
          val (newResult, _) = encryptedClientSideSessionFactory.newSession(r)
          val cookiesAfter = fetchCookiesFromHeaders(newResult)
          cookiesAfter.size should equal(1) // We expect the new result to contain the session secret cookie

          val sessionSecretKeyCookie = cookiesAfter.head
          val cookieNameExtractedFromValue = sessionSecretKeyCookie.value.substring(0, sessionSecretKeyCookie.name.length)
          cookieNameExtractedFromValue should equal(sessionSecretKeyCookie.name)
      }
    }

    "return session containing a populated new session secret cookie" in new WithApplication {
      val request = FakeCSRFRequest()
      val result = setUpTradeDetails.present(request)

      whenReady(result) {
        r =>
          val encryptedClientSideSessionFactory = new EncryptedClientSideSessionFactory()(noCookieFlags, noEncryption, noHashing)
          val (newResult, session) = encryptedClientSideSessionFactory.newSession(r)
          val cookiesAfter = fetchCookiesFromHeaders(newResult)
          val sessionSecretKeyCookie = cookiesAfter.head
          session.getCookieValue(sessionSecretKeyCookie) should not equal ""
      }
    }

    "return None when trying to fetch the client session from the request object when there are no cookies" in new WithApplication {
      val request = FakeCSRFRequest()
      val encryptedClientSideSessionFactory = new EncryptedClientSideSessionFactory()(noCookieFlags, noEncryption, noHashing)
      val session = encryptedClientSideSessionFactory.getSession(request.cookies)
      session should equal(None)
    }

    "return a client side session when trying to fetch the session from the request object that contains a cookie" in new WithApplication {
      implicit val request = FakeCSRFRequest()
      implicit val encryptedClientSideSessionFactory = new EncryptedClientSideSessionFactory()(noCookieFlags, noEncryption, noHashing)
      val result = setUpTradeDetails.present(request)

      whenReady(result) {
        r =>
          val cookies = fetchCookiesFromHeaders(r)
          cookies.size should equal(0)
          val newResult = r.withCookie("key", "value") // Add a cookie to the result. The side effect of this will be to also add the session cookie
          val newResultCookies = fetchCookiesFromHeaders(newResult)
          newResultCookies.size should equal(2) // We now expect the result to contain the encrypted cookie and the session secret cookie
      }
    }

  }

  implicit val noCookieFlags = new NoCookieFlags
  implicit val noEncryption = new NoEncryption with CookieEncryption
  implicit val noHashing = new NoHash with CookieNameHashing

  private val setUpTradeDetails = injector.getInstance(classOf[SetUpTradeDetails])
}
