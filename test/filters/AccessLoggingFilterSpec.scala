package filters

import helpers.UnitSpec
import common.ClientSideSessionFactory
import play.api.mvc._
import scala.concurrent.{ExecutionContext, Future}
import play.api.test.FakeRequest
import ExecutionContext.Implicits.global
import scala.language.existentials
import play.api.http.HeaderNames._
import play.api.mvc.Cookie
import play.api.mvc.SimpleResult
import services.HttpHeaders

class AccessLoggingFilterSpec extends UnitSpec {

  "Log an incoming request" in setUp {
    case SetUp(filter, request, sessionFactory, nextFilter) =>

      val trackingIdCookie = Cookie("trackingId", "trackingIdValue")

      val filterResult: Future[SimpleResult] = filter.apply(nextFilter)(request.withCookies(trackingIdCookie))

      whenReady(filterResult) { result =>
        info("The log entry should look like 127.0.0.1 - - [dd/MMM/yyyy:hh:mm:ss +SSS] \"GET / HTTP/1.1\" 200 12345 \"98765\"")
        println(filter.logEntry)
        filter.logEntry should include ("127.0.0.1")
        filter.logEntry should include ("GET /")
        filter.logEntry should include ("HTTP/1.1")
        filter.logEntry should include ("200")
        filter.logEntry should include ("12345")
      }

  }

  private class MockFilter extends ((RequestHeader) => Future[SimpleResult]) {
    var passedRequest: RequestHeader = _

    override def apply(rh: RequestHeader): Future[SimpleResult] = {
      passedRequest = rh
      Future(Results.Ok)
    }
  }

  private case class SetUp(filter: testAccessLoggingFilter,
                           request: FakeRequest[_],
                           sessionFactory:ClientSideSessionFactory,
                           nextFilter: MockFilter)

  private def setUp(test: SetUp => Any) {
    val sessionFactory = mock[ClientSideSessionFactory]

    test(SetUp(
      filter = new testAccessLoggingFilter,
      request = FakeRequest(),
      sessionFactory = sessionFactory,
      nextFilter = new MockFilter()
    ))
  }


  class testAccessLoggingFilter extends AccessLoggingFilter{
    var logEntry = ""
    override def apply(filter: (RequestHeader) => Future[SimpleResult])(requestHeader: RequestHeader): Future[SimpleResult] = {
      filter(requestHeader)
        .map (
        result => {
          val extendedResult = result.withHeaders(CONTENT_LENGTH -> "12345").withHeaders(HttpHeaders.TrackingId -> "98765")
          logEntry = clfEntry(requestHeader, extendedResult)
          result
        }
      )
    }
  }

}
