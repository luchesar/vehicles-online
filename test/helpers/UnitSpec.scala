package helpers

import org.scalatest.{Matchers, WordSpec}
import org.scalatest.mock.MockitoSugar
import org.scalatest.concurrent.ScalaFutures
import play.api.test.FakeRequest
import play.api.mvc.AnyContentAsEmpty
import play.filters.csrf._
import org.scalatest.concurrent.PatienceConfiguration.Timeout
import org.scalatest.time.{Second, Span}

abstract class UnitSpec extends WordSpec with Matchers with MockitoSugar with ScalaFutures {
  protected val timeout = Timeout(Span(1, Second))

  object FakeCSRFRequest {
    def apply(): FakeRequest[AnyContentAsEmpty.type] = {
      FakeRequest()
        .withSession("csrfToken" -> CSRF.SignedTokenProvider.generateToken)
    }
  }

}