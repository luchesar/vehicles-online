package services.address_lookup.ordnance_survey

import helpers.UnitSpec
import services.fakes.FakeAddressLookupService._
import utils.helpers.Config
import org.scalatest.BeforeAndAfterEach
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.core.WireMockConfiguration._
import com.github.tomakehurst.wiremock.http.{Response, Request, RequestListener}
import common.{NoCookieFlags, ClearTextClientSideSession, ClientSideSession, ClientSideSessionFactory}
import scala.collection.mutable
import java.net.ServerSocket
import org.scalatest.time.SpanSugar._
import org.scalatest.concurrent.PatienceConfiguration.{Interval, Timeout}
import play.api.i18n.Lang

final class WebServiceImplSpec extends UnitSpec with BeforeAndAfterEach {

  val wireMockPort: Int = {
    val serverSocket = new ServerSocket(0)
    try serverSocket.getLocalPort
    catch{ case e:Exception => 51987}
    finally serverSocket.close()
  }

  val wireMockServer = new WireMockServer(wireMockConfig().port(wireMockPort))
  val trackingIdValue = "trackingIdValue"
  val interval = Interval(50 millis)

  implicit val noCookieFlags = new NoCookieFlags
  implicit val clientSideSession: ClientSideSession = new ClearTextClientSideSession(trackingIdValue)

  override def beforeEach() {
    wireMockServer.start()
  }

  override def afterEach() {
    wireMockServer.stop()
  }

  import composition.TestComposition.{testInjector => injector}
  implicit val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
  val addressLookupService = new services.address_lookup.ordnance_survey.WebServiceImpl(new Config() {
    override val ordnanceSurveyMicroServiceUrl = s"http://localhost:$wireMockPort"
  })

  "postcodeWithNoSpaces" should {

    "return the same string if no spaces present" in {
      val result = addressLookupService.postcodeWithNoSpaces(PostcodeValid)

      result should equal(PostcodeValid)
    }

    "remove spaces when present" in {
      val result = addressLookupService.postcodeWithNoSpaces(PostcodeValidWithSpace)

      result should equal(PostcodeValid)
    }
  }

  "WebServiceImplSpec" ignore {
    "send the trackingId to the PostcodeWebService" in {
      val sentRequestsUrls = addRequestListener()

      val postCode = "N193NN"

      val futureResult = addressLookupService.callPostcodeWebService(postCode)(clientSideSession, lang = Lang("en"))

      whenReady(futureResult, timeout, interval) { result =>
        sentRequestsUrls should have size 1
        sentRequestsUrls(0) should include(s"?postcode=$postCode")
        sentRequestsUrls(0) should include(s"&tracking_id=$trackingIdValue")
      }
    }

    "send the trackingId to the callUprnWebService" in {
      val sentRequestsUrls = addRequestListener()

      val postCode = "N193NN"

      val futureResult = addressLookupService.callUprnWebService(postCode)(clientSideSession, Lang("en"))

      whenReady(futureResult, timeout, interval) { result =>
        sentRequestsUrls should have size 1
        sentRequestsUrls(0) should include(s"?uprn=$postCode")
        sentRequestsUrls(0) should include(s"&tracking_id=$trackingIdValue")
      }
    }
  }

  private def addRequestListener(): mutable.ArrayBuffer[String] =  {
    var sentRequestsUrls: mutable.ArrayBuffer[String] =  mutable.ArrayBuffer.empty[String]

    wireMockServer.addMockServiceRequestListener(new RequestListener(){
      override def requestReceived(request: Request, response: Response): Unit = {
        sentRequestsUrls += request.getUrl
      }
    })

    sentRequestsUrls
  }
}
