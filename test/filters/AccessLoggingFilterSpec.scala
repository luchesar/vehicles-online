package filters

import common.ClientSideSessionFactory
import helpers.UnitSpec
import play.api.mvc._
import play.api.test.{FakeHeaders, FakeRequest}
import play.api.http.HeaderNames._
import services.HttpHeaders

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.language.existentials

class ClfLoggerSpec extends UnitSpec {

  "Log an incoming request" in setUp() {
    case SetUp(filter, request, sessionFactory, nextFilter) =>

      val trackingIdCookie = Cookie(ClientSideSessionFactory.TrackingIdCookieName, "98765")

      val filterResult: Future[SimpleResult] = filter.apply(nextFilter)(request.withCookies(trackingIdCookie))

      whenReady(filterResult) { result =>
        info("The log entry should look like 127.0.0.1 - - [dd/MMM/yyyy:hh:mm:ss +SSS] \"GET / HTTP/1.1\" 200 12345 \"98765\"")
        filter.logEntry should startWith ("127.0.0.1")
        filter.logEntry should include ("GET /")
        filter.logEntry should include ("HTTP/1.1")
        filter.logEntry should include ("200")
        filter.logEntry should include ("12345")
        filter.logEntry should include ("98765")
      }

  }

  "Log an incoming request with ipAddress provided in XForwardedFor only" in setUp("127.0.0.3") {
    case SetUp(filter, request, sessionFactory, nextFilter) =>

      val filterResult: Future[SimpleResult] = filter.apply(nextFilter)(
        request
          .withHeaders(HttpHeaders.XForwardedFor -> "127.0.0.2")
          .withHeaders(HttpHeaders.XRealIp -> "127.0.0.4")
      )

      whenReady(filterResult) { result =>
        filter.logEntry should startWith ("127.0.0.2")
      }

  }

  "Log an incoming request with ipAddress provided in remoteAddress only" in setUp("127.0.0.3") {
    case SetUp(filter, request, sessionFactory, nextFilter) =>

      val filterResult: Future[SimpleResult] = filter.apply(nextFilter)(request.withHeaders(HttpHeaders.XRealIp -> "127.0.0.4"))

      whenReady(filterResult) { result =>
        filter.logEntry should startWith ("127.0.0.3")
      }

  }

  "Log an incoming request with ipAddress provided in XRealIp only" in setUp(null) {
    case SetUp(filter, request, sessionFactory, nextFilter) =>

      val filterResult: Future[SimpleResult] = filter.apply(nextFilter)(request.withHeaders(HttpHeaders.XRealIp -> "127.0.0.4"))

      whenReady(filterResult) { result =>
        filter.logEntry should startWith ("127.0.0.4")
      }

  }

  "Log an incoming request with no ipAddress provided" in setUp(null) {
    case SetUp(filter, request, sessionFactory, nextFilter) =>

      val filterResult: Future[SimpleResult] = filter.apply(nextFilter)(request)

      whenReady(filterResult) { result =>
        filter.logEntry should startWith ("-")
      }

  }

  private class MockFilter extends ((RequestHeader) => Future[SimpleResult]) {
    var passedRequest: RequestHeader = _

    override def apply(rh: RequestHeader): Future[SimpleResult] = {
      passedRequest = rh
      Future(Results.Ok)
    }
  }

  private case class SetUp(filter: TestAccessLoggingFilter,
                           request: FakeRequest[_],
                           sessionFactory:ClientSideSessionFactory,
                           nextFilter: MockFilter)

  private def setUp(ipAddress: String = "127.0.0.1")(test: SetUp => Any) {
    val sessionFactory = mock[ClientSideSessionFactory]

    test(SetUp(
      filter = testAccessLoggingFilter,
      request = FakeRequest("GET", "/", FakeHeaders(), AnyContentAsEmpty, remoteAddress = ipAddress),
      sessionFactory = sessionFactory,
      nextFilter = new MockFilter()
    ))
  }

  private class TestClfLogger extends ClfLogger {
    var entry = ""

    override def clfEntry(request: RequestHeader, result: SimpleResult) : String = {
      val extendedResult = result.withHeaders(CONTENT_LENGTH -> "12345").
        withHeaders(ClientSideSessionFactory.TrackingIdCookieName -> "98765")
      entry = super.clfEntry(request, extendedResult)
      entry
    }
  }

  private class TestAccessLoggingFilter(val logger: TestClfLogger) extends AccessLoggingFilter(logger) {
    def logEntry = logger.entry
  }

  private def testAccessLoggingFilter: TestAccessLoggingFilter = new TestAccessLoggingFilter(new TestClfLogger())
//  {
//    var logEntry = ""
//    override def apply(filter: (RequestHeader) => Future[SimpleResult])(requestHeader: RequestHeader): Future[SimpleResult] = {
//      filter(requestHeader)
//        .map (
//        result => {
//          val extendedResult = result.withHeaders(CONTENT_LENGTH -> "12345").withHeaders(ClientSideSessionFactory.TrackingIdCookieName -> "98765")
//          logEntry = clfEntry(requestHeader, extendedResult)
//          result
//        }
//      )
//    }
//  }

}
