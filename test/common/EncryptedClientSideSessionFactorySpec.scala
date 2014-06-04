package common

import utils.helpers._
import composition.TestComposition.{testInjector => injector}
import helpers.{WithApplication, UnitSpec}
import controllers.disposal_of_vehicle.SetUpTradeDetails
import common.CookieHelper._
import common.CookieImplicits.SimpleResultAdapter
import play.api.mvc.{Cookies, Cookie}
import play.api.http.HeaderNames

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
          val (newResult, _) = encryptedClientSideSessionFactory.ensureSession(request.cookies, r)
          val cookiesAfter = fetchCookiesFromHeaders(newResult)
          cookiesAfter.size should equal(2  ) // We expect the new result to contain the session secret cookie and the trackingId cookie

          val (trackingId, sessionSecretKeySuffix) = fetchSessionCookies(cookiesAfter)

          trackingId.value should have size 20

          val sessionSecretKey = trackingId.value + sessionSecretKeySuffix.value
          val cookieNameExtractedFromValue = sessionSecretKey.substring(0, sessionSecretKeySuffix.name.length)
          cookieNameExtractedFromValue should equal(sessionSecretKeySuffix.name)

      }
    }

    "not create session again if the session is already created in the SimpleResult" in new WithApplication {
      val request = FakeCSRFRequest()
      val result = setUpTradeDetails.present(request)

      whenReady(result) {
        r =>
          val encryptedClientSideSessionFactory = new EncryptedClientSideSessionFactory()(noCookieFlags, noEncryption, noHashing)
          val (result, session1) = encryptedClientSideSessionFactory.ensureSession(request.cookies, r)
          result.withSession()
          val cookies = Cookies.decode(result.header.headers(HeaderNames.SET_COOKIE))
          val newRequestCookies = request.cookies ++ cookies
          val (_, session2) = encryptedClientSideSessionFactory.ensureSession(newRequestCookies, result)


          session1 should equal(session2)
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
          newResultCookies.size should equal(3) // We now expect the result to contain the encrypted cookie, trackingId cookie and the session secret cookie
      }
    }

  }

  private def fetchSessionCookies(cookies: Seq[Cookie]): (Cookie, Cookie) = {
    val trackingId = cookies.find(_.name == "tracking-id").get
    val sessionSecretKeySuffix = cookies.find(_ != trackingId).get
    (trackingId, sessionSecretKeySuffix)
  }

  implicit val noCookieFlags = new NoCookieFlags
  implicit val noEncryption = new NoEncryption with CookieEncryption
  implicit val noHashing = new NoHash with CookieNameHashing

  private val setUpTradeDetails = injector.getInstance(classOf[SetUpTradeDetails])
}
