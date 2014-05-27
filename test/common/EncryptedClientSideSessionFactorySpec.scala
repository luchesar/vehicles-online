package common

import play.api.test.{FakeRequest, WithApplication}
import utils.helpers._
import composition.TestComposition.{testInjector => injector}
import helpers.UnitSpec
import controllers.disposal_of_vehicle.SetUpTradeDetails
import common.EncryptedCookieImplicitsHelper.SimpleResultAdapter

final class EncryptedClientSideSessionFactorySpec extends UnitSpec {
  "newSession" should {
    "return result containing a new session secret cookie" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = setUpTradeDetails.present(request)

      whenReady(result) {
        r =>
          val cookiesBefore = r.fetchCookiesFromHeaders
          cookiesBefore.size should equal(0) // There should be no cookies in the result before we call the factory
          val encryptedClientSideSessionFactory = new EncryptedClientSideSessionFactory()(noCookieFlags, noEncryption, noHashing)
          val (newResult, _) = encryptedClientSideSessionFactory.newSession(request, r)
          val cookiesAfter = newResult.fetchCookiesFromHeaders
          cookiesAfter.size should equal(1) // We expect the new result to contain the session secret cookie

          val sessionSecretKeyCookie = cookiesAfter.head
          val cookieNameExtractedFromValue = sessionSecretKeyCookie.value.substring(0, sessionSecretKeyCookie.name.length)
          cookieNameExtractedFromValue should equal(sessionSecretKeyCookie.name)
      }
    }

    "return session containing a populated new session secret cookie" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = setUpTradeDetails.present(request)

      whenReady(result) {
        r =>
          val encryptedClientSideSessionFactory = new EncryptedClientSideSessionFactory()(noCookieFlags, noEncryption, noHashing)

          val (newResult, session) = encryptedClientSideSessionFactory.newSession(request, r)

          val cookiesAfter = newResult.fetchCookiesFromHeaders
          val sessionSecretKeyCookie = cookiesAfter.head
          session.getCookieValue(sessionSecretKeyCookie) should not equal ""
      }
    }

    "return None when trying to fetch the client session from the request object when there are no cookies" in new WithApplication {
      val request = FakeRequest().withSession()
      val encryptedClientSideSessionFactory = new EncryptedClientSideSessionFactory()(noCookieFlags, noEncryption, noHashing)
      val session = encryptedClientSideSessionFactory.getSession(request)
      session should equal(None)
    }
  }

  implicit val noCookieFlags = new NoCookieFlags
  implicit val noEncryption = new NoEncryption with CookieEncryption
  implicit val noHashing = new NoHash with CookieNameHashing

  private val setUpTradeDetails = injector.getInstance(classOf[SetUpTradeDetails])
}
