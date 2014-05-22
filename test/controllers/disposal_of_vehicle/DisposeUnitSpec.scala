package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import mappings.disposal_of_vehicle.Dispose._
import models.domain.disposal_of_vehicle._
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import org.mockito.Mockito._
import org.mockito.Matchers._
import helpers.UnitSpec
import services.dispose_service.{DisposeServiceImpl, DisposeWebService, DisposeService}
import services.fakes.{FakeDisposeWebServiceImpl, FakeResponse}
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.Json
import ExecutionContext.Implicits.global
import services.{DateService, DateServiceImpl}
import services.fakes.FakeDateServiceImpl._
import services.fakes.FakeDisposeWebServiceImpl._
import play.api.mvc.Cookies
import utils.helpers.{CookieNameHashing, NoHash, CookieEncryption, NoEncryption}
import models.DayMonthYear
import scala.Some
import services.fakes.FakeAddressLookupService._
import scala.Some
import org.joda.time.format.ISODateTimeFormat

final class DisposeUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeController().present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to setupTradeDetails page when present and previous pages have not been visited" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = disposeController().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "display populated fields when cookie exists" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeFormModel())
      val result = disposeController().present(request)
      val content = contentAsString(result)
      val contentWithCarriageReturnsAndSpacesRemoved = content.replaceAll("[\n\r]", "").replaceAll(emptySpace, "")
      contentWithCarriageReturnsAndSpacesRemoved should include(buildCheckboxHtml("consent", true))
      contentWithCarriageReturnsAndSpacesRemoved should include(buildCheckboxHtml("lossOfRegistrationConsent", true))

      contentWithCarriageReturnsAndSpacesRemoved should include(buildSelectedOptionHtml("25", "25"))
      contentWithCarriageReturnsAndSpacesRemoved should include(buildSelectedOptionHtml("11", "November"))
      contentWithCarriageReturnsAndSpacesRemoved should include(buildSelectedOptionHtml("1970", "1970"))
    }

    "display empty fields when cookie does not exist" in new WithApplication {
      val request = FakeRequest().withSession().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeController().present(request)
      val content = contentAsString(result)
      val contentWithCarriageReturnsAndSpacesRemoved = content.replaceAll("[\n\r]", "").replaceAll(emptySpace, "")
      contentWithCarriageReturnsAndSpacesRemoved should include(buildCheckboxHtml("consent", false))
      contentWithCarriageReturnsAndSpacesRemoved should include(buildCheckboxHtml("lossOfRegistrationConsent", false))
      content should not include "selected" // No drop downs should be selected
    }
  }

  private def buildCheckboxHtml(widgetName: String, checked: Boolean) : String = {
    if (checked)
      s"""<inputtype="checkbox"id="$widgetName"name="$widgetName"value="true"checkedaria-required=true>"""
    else
      s"""<inputtype="checkbox"id="$widgetName"name="$widgetName"value="true"aria-required=true>"""
  }

  private def buildSelectedOptionHtml(optionValue: String, optionText: String) : String = {
    s"""<optionvalue="$optionValue"selected>$optionText</option>"""
  }

  "submit" should {
    "build a dispose request when cookie contains all required data" in {
      val traderName = "TestTraderName"
      val traderAddress = AddressViewModel(Some(12345), Seq("Line1Val", "AA11AA"))
      val expectedDisposalAddress = DisposalAddressDto(Seq("LineVal1"),None, "AA11AA", Some(12345))
      val referenceNumber = "01234567890"
      val registrationNumber = "AA111AAA"
      val mileage = Some(2000)
      val dateOfDisposal = DayMonthYear(1, 1, 2014)
      val expetedDateOfDisposal = "2014-01-01T00:00:00.000Z"
      val expetedTimestamp = "2014-01-01T00:00:00.000Z"
      val ipAddress = None

      val noCookieEncryption = new NoEncryption with CookieEncryption
      val noCookieNameHashing = new NoHash with CookieNameHashing

      val disposeModel = DisposeModel("01234567890", "AA111AAA", dateOfDisposal, mileage)
      val traderModel = TraderDetailsModel(traderName, traderAddress)
      val disposeClient = new disposal_of_vehicle.Dispose(mock[DisposeService], dateServiceStubbed())(noCookieEncryption, noCookieNameHashing)
      val disposeResponse = buildDisposeMicroServiceRequest(disposeModel, traderModel)

      disposeResponse.referenceNumber should equal(referenceNumber)
      disposeResponse.registrationNumber should equal(registrationNumber)
      disposeResponse.traderName should equal(traderName)
      disposeResponse.disposalAddress.postCode should equal(expectedDisposalAddress.postCode)
      disposeResponse.dateOfDisposal should equal(expetedDateOfDisposal)
      disposeResponse.mileage should equal(mileage)
      disposeResponse.transactionTimestamp should equal (expetedTimestamp)
      disposeResponse.ipAddress should equal(ipAddress)
    }

    "redirect to dispose success when a success message is returned by the fake microservice" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())

        val result = disposeController().submit(request)

      redirectLocation(result) should equal(Some(DisposeSuccessPage.address))
      whenReady(result) {
        r =>
          r.header.headers.get(LOCATION) should equal(Some(DisposeSuccessPage.address))
      }
    }

    "redirect to micro-service error page when an unexpected error occurs" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val disposeFailure = disposeController(disposeServiceStatus = INTERNAL_SERVER_ERROR, disposeServiceResponse = None)
      val result = disposeFailure.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(MicroServiceErrorPage.address))
      }
    }

    "redirect to setupTradeDetails page after the dispose button is clicked and no vehicleLookupFormModel is cached" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest.withCookies(CookieFactoryForUnitSpecs.setupTradeDetails())
      val result = disposeController().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "return a bad request when no details are entered" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeController().submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "redirect to setupTradeDetails page when form submitted with errors and previous pages have not been visited" in new WithApplication {
      val request = FakeRequest().withSession().withFormUrlEncodedBody()
      val result = disposeController().submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to micro-service error page when calling webservice throws exception" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val disposeResponseThrows = mock[(Int, Option[DisposeResponse])]
      val mockWebServiceThrows = mock[DisposeService]
      when(mockWebServiceThrows.invoke(any[DisposeRequest])).thenReturn(Future.failed(new RuntimeException))
      val noCookieEncryption = new NoEncryption with CookieEncryption
      val noCookieNameHashing = new NoHash with CookieNameHashing
      val dispose = new disposal_of_vehicle.Dispose(mockWebServiceThrows, dateServiceStubbed())(noCookieEncryption, noCookieNameHashing)
      val result = dispose.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(MicroServiceErrorPage.address))
      }
    }

    "redirect to soap endpoint error page when service is unavailable" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val disposeFailure = disposeController(disposeServiceStatus = SERVICE_UNAVAILABLE, disposeServiceResponse = None)
      val result = disposeFailure.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SoapEndpointErrorPage.address))
      }
    }

    "redirect to dispose success when applicationBeingProcessed" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val disposeSuccess = disposeController(disposeServiceResponse = Some(disposeResponseApplicationBeingProcessed))
      val result = disposeSuccess.submit(request)
      redirectLocation(result) should equal(Some(DisposeSuccessPage.address))
    }

    "redirect to dispose failure page when unableToProcessApplication" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val disposeFailure = disposeController(disposeServiceResponse = Some(disposeResponseUnableToProcessApplication))
      val result = disposeFailure.submit(request)
      whenReady(result) {
        r => {
          r.header.headers.get(LOCATION) should equal(Some(DisposeFailurePage.address))
        }
      }
    }

    "redirect to error page when undefined error is returned" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val disposeFailure = disposeController(disposeServiceResponse = Some(disposeResponseUndefinedError))
      val result = disposeFailure.submit(request)

      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(MicroServiceErrorPage.address))
      }
    }

    "write cookies when a success message is returned by the fake microservice" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val result = disposeController().submit(request)
      whenReady(result) {
        r =>
          val cookies = r.header.headers.get(SET_COOKIE).toSeq.flatMap(Cookies.decode)
          val found = cookies.find(_.name == DisposeFormTimestampIdCacheKey)
          found match {
            case Some(cookie) => cookie.value should include (CookieFactoryForUnitSpecs.disposeFormTimestamp().value)
            case _ => fail("Should have found cookie")
          }
          cookies.map(_.name) should contain allOf (DisposeModelCacheKey, DisposeFormTransactionIdCacheKey, DisposeFormModelCacheKey, DisposeFormTimestampIdCacheKey)
      }
    }

    "write cookies when applicationBeingProcessed" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val disposeSuccess = disposeController(disposeServiceResponse = Some(disposeResponseApplicationBeingProcessed))
      val result = disposeSuccess.submit(request)
      whenReady(result) {
        r =>
          val cookies = r.header.headers.get(SET_COOKIE).toSeq.flatMap(Cookies.decode)
          cookies.map(_.name) should contain allOf (DisposeModelCacheKey, DisposeFormTransactionIdCacheKey, DisposeFormRegistrationNumberCacheKey, DisposeFormModelCacheKey, DisposeFormTimestampIdCacheKey)
      }
    }
  }

  private val emptySpace = " "

  private def dateServiceStubbed(day: Int = dateOfDisposalDayValid.toInt,
                                 month: Int = dateOfDisposalMonthValid.toInt,
                                 year: Int = dateOfDisposalYearValid.toInt) = {
    val dateService = mock[DateService]
    when(dateService.today).thenReturn(new models.DayMonthYear(day = day,
      month = month,
      year = year))
    dateService
  }

  private def buildCorrectlyPopulatedRequest = {
    import mappings.common.DayMonthYear._
    FakeRequest().withSession().withFormUrlEncodedBody(
      MileageId -> mileageValid,
      s"$DateOfDisposalId.$DayId" -> dateOfDisposalDayValid,
      s"$DateOfDisposalId.$MonthId" -> dateOfDisposalMonthValid,
      s"$DateOfDisposalId.$YearId" -> dateOfDisposalYearValid,
      ConsentId -> consentValid,
      LossOfRegistrationConsentId -> consentValid
    )
  }

  private def disposeController(disposeServiceStatus: Int = OK,
                                disposeServiceResponse: Option[DisposeResponse] = Some(disposeResponseSuccess)) = {
    val ws = mock[DisposeWebService]
    when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
      val fakeJson = disposeServiceResponse map (Json.toJson(_))
      new FakeResponse(status = disposeServiceStatus, fakeJson = fakeJson) // Any call to a webservice will always return this successful response.
    })
    val disposeServiceImpl = new DisposeServiceImpl(ws)
    val noCookieEncryption = new NoEncryption with CookieEncryption
    val noCookieNameHashing = new NoHash with CookieNameHashing
    new disposal_of_vehicle.Dispose(disposeServiceImpl, dateServiceStubbed())(noCookieEncryption, noCookieNameHashing)
  }

  private def buildDisposeMicroServiceRequest(disposeModel: DisposeModel, traderDetails: TraderDetailsModel): DisposeRequest = {
    val dateTime = disposeModel.dateOfDisposal.toDateTime.get
    val formatter = ISODateTimeFormat.dateTime()
    val isoDateTimeString = formatter.print(dateTime)

    DisposeRequest(referenceNumber = disposeModel.referenceNumber,
      registrationNumber = disposeModel.registrationNumber,
      traderName = traderDetails.traderName,
      disposalAddress = disposalAddressDto(traderDetails.traderAddress),
      dateOfDisposal = isoDateTimeString,
      transactionTimestamp = ISODateTimeFormat.dateTime().print(dateTime),
      mileage = disposeModel.mileage,
      ipAddress = None)
  }

  private def disposalAddressDto(sourceAddress: AddressViewModel): DisposalAddressDto = {
    // The last two address lines are always post town and postcode
    val postAddressLines = sourceAddress.address.dropRight(2)
    val postTown = sourceAddress.address.takeRight(2).head
    val postcode = sourceAddress.address.last

    DisposalAddressDto(postAddressLines, Some(postTown), postcode, sourceAddress.uprn)
  }

}
