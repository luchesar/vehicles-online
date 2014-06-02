package filters

import helpers.UnitSpec
import common.{ClientSideSession, ClientSideSessionFactory}
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import play.api.mvc.Cookie
import play.api.mvc.SimpleResult
import org.mockito.Mockito._
import ExecutionContext.Implicits.global

class EnsureSessionCreatedFilterSpec extends UnitSpec {
  val sessionFactory = mock[ClientSideSessionFactory]
  val result = mock[SimpleResult]
  val generatedCookie = Cookie("keyFromNext", "valueFromNext")
  val result2 = result.withCookies(generatedCookie)
  val resultSession = mock[ClientSideSession]
  val finalResult = mock[SimpleResult]

  val nextFilter = (r: RequestHeader) => Future {
    result2
  }
  val requestHeader = mock[RequestHeader]
  when(sessionFactory.ensureSession(requestHeader.cookies, result2)).thenReturn((finalResult, resultSession))

  val filter = new EnsureSessionCreatedFilter(sessionFactory)

  "EnsureSessionCreatedFilterSpec" should {
    "Create a session if there is not one" in {
      val filterResult = filter.apply(nextFilter)(requestHeader)

      whenReady(filterResult) {
        result => result should equal(finalResult)
      }
    }
  }
}
