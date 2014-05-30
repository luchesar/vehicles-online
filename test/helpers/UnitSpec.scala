package helpers

import org.scalatest.{Matchers, WordSpec}
import org.scalatest.mock.MockitoSugar
import org.scalatest.concurrent.ScalaFutures
import play.api.test.FakeRequest
import play.api.mvc.AnyContentAsEmpty
import play.filters.csrf._

abstract class UnitSpec extends WordSpec with Matchers with MockitoSugar with ScalaFutures {

  object FakeCSRFRequest {
    def apply(): FakeRequest[AnyContentAsEmpty.type] = {
      FakeRequest()
        .withSession("csrfToken" -> CSRF.SignedTokenProvider.generateToken)
    }
  }

}