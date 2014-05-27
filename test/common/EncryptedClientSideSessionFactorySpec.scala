package common

import play.api.test.{FakeRequest, WithApplication}
import utils.helpers._
import composition.{testInjector => injector}
import helpers.UnitSpec
import play.api.mvc.Cookies
import controllers.disposal_of_vehicle.SetUpTradeDetails

class EncryptedClientSideSessionFactorySpec extends UnitSpec {

  implicit val noCookieFlags = new NoCookieFlags
  implicit val noEncryption = new NoEncryption with CookieEncryption
  implicit val noHashing = new NoHash with CookieNameHashing

  private val setUpTradeDetails = injector.getInstance(classOf[SetUpTradeDetails])

  "EncryptedClientSideSessionFactory" should {
    "return a tuple consisting of a new session secret cookie in the result along with a new client side session after calling newSession" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = setUpTradeDetails.present(request)

      whenReady(result) {
        r =>
          val cookiesBefore = r.header.headers.get(play.api.http.HeaderNames.SET_COOKIE).toSeq.flatMap(Cookies.decode)
          cookiesBefore.size should equal(0) // There should be no cookies in the result before we call the factory
          val encryptedClientSideSessionFactory = new EncryptedClientSideSessionFactory()(noCookieFlags, noEncryption, noHashing)
          val (newResult, session) = encryptedClientSideSessionFactory.newSession(request, r)
          val cookiesAfter = newResult.header.headers.get(play.api.http.HeaderNames.SET_COOKIE).toSeq.flatMap(Cookies.decode)
          cookiesAfter.size should equal(1) // We expect the result to contain the session secret cookie

          val sessionSecretKeyCookie = cookiesAfter.head
          sessionSecretKeyCookie.name should equal(sessionSecretKeyCookie.value.substring(0, sessionSecretKeyCookie.name.length))
          session.getCookieValue(sessionSecretKeyCookie) should not equal("")
      }
    }

    "return None when trying to fetch the client session from the request object when there are no cookies" in new WithApplication {
      val request = FakeRequest().withSession()
      val encryptedClientSideSessionFactory = new EncryptedClientSideSessionFactory()(noCookieFlags, noEncryption, noHashing)
      val session = encryptedClientSideSessionFactory.getSession(request)
      session should equal(None)
    }
  }
}
