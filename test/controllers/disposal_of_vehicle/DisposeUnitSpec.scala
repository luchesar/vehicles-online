package controllers.disposal_of_vehicle

import common.ClientSideSessionFactory
import common.ClearTextClientSideSessionFactory.DefaultTrackingId
import common.CookieHelper._
import composition.TestComposition.{testInjector => injector}
import controllers.disposal_of_vehicle
import helpers.UnitSpec
import helpers.WithApplication
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import mappings.disposal_of_vehicle.Dispose._
import models.domain.disposal_of_vehicle._
import org.mockito.Matchers._
import org.mockito.Mockito._
import pages.disposal_of_vehicle._
import play.api.libs.json.Json
import play.api.test.Helpers._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.DateService
import services.dispose_service.{DisposeServiceImpl, DisposeWebService, DisposeService}
import services.fakes.FakeAddressLookupService._
import services.fakes.FakeDisposeWebServiceImpl._
import services.fakes.FakeResponse
import services.fakes.FakeVehicleLookupWebService._
import mappings.common.AddressLines.LineMaxLength
import services.fakes.FakeDateServiceImpl._
import scala.Some
import services.fakes.FakeDisposeWebServiceImpl.ConsentValid
import models.DayMonthYear
import utils.helpers.Config
import mappings.common.AddressLines.BuildingNameOrNumberHolder
import play.api.test.FakeRequest

final class DisposeUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeController().present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to setupTradeDetails page when present and previous pages have not been visited" in new WithApplication {
      val request = FakeRequest()
      val result = disposeController().present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "display populated fields when cookie exists" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeFormModel())
      val result = disposeController().present(request)
      val content = contentAsString(result)
      val contentWithCarriageReturnsAndSpacesRemoved = content.replaceAll("[\n\r]", "").replaceAll(emptySpace, "")
      contentWithCarriageReturnsAndSpacesRemoved should include(buildCheckboxHtml("consent", checked = true))
      contentWithCarriageReturnsAndSpacesRemoved should include(buildCheckboxHtml("lossOfRegistrationConsent", checked = true))

      contentWithCarriageReturnsAndSpacesRemoved should include(buildSelectedOptionHtml("25", "25"))
      contentWithCarriageReturnsAndSpacesRemoved should include(buildSelectedOptionHtml("11", "November"))
      contentWithCarriageReturnsAndSpacesRemoved should include(buildSelectedOptionHtml("1970", "1970"))
    }

    "display empty fields when cookie does not exist" in new WithApplication {
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.setupTradeDetails()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeController().present(request)
      val content = contentAsString(result)
      val contentWithCarriageReturnsAndSpacesRemoved = content.replaceAll("[\n\r]", "").replaceAll(emptySpace, "")
      contentWithCarriageReturnsAndSpacesRemoved should include(buildCheckboxHtml("consent", checked = false))
      contentWithCarriageReturnsAndSpacesRemoved should include(buildCheckboxHtml("lossOfRegistrationConsent", checked = false))
      content should not include "selected" // No drop downs should be selected
    }
  }


  "submit" should {
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

    "redirect to duplicate-disposal error page when an duplicate disposal error occurs" in new WithApplication {
      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.disposeModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
      val disposeFailure = disposeController(disposeServiceStatus = OK, disposeServiceResponse = Some(disposeResponseFailureWithDuplicateDisposal))
      val result = disposeFailure.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(DuplicateDisposalErrorPage.address))
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
      val request = FakeRequest().withFormUrlEncodedBody().
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel())
      val result = disposeController().submit(request)
      whenReady(result) {
        r => r.header.status should equal(BAD_REQUEST)
      }
    }

    "redirect to setupTradeDetails page when form submitted with errors and previous pages have not been visited" in new WithApplication {
      val request = FakeRequest().withFormUrlEncodedBody()
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
      val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
      val dispose = new disposal_of_vehicle.Dispose(mockWebServiceThrows, dateServiceStubbed())(clientSideSessionFactory)
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

      whenReady(result, timeout) {
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
          val cookies = fetchCookiesFromHeaders(r)
          val found = cookies.find(_.name == DisposeFormTimestampIdCacheKey)
          found match {
            case Some(cookie) => cookie.value should include(CookieFactoryForUnitSpecs.disposeFormTimestamp().value)
            case _ => fail("Should have found cookie")
          }
          cookies.map(_.name) should contain allOf(DisposeModelCacheKey, DisposeFormTransactionIdCacheKey, DisposeFormModelCacheKey, DisposeFormTimestampIdCacheKey)
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
          val cookies = fetchCookiesFromHeaders(r)
          cookies.map(_.name) should contain allOf(DisposeModelCacheKey, DisposeFormTransactionIdCacheKey, DisposeFormRegistrationNumberCacheKey, DisposeFormModelCacheKey, DisposeFormTimestampIdCacheKey)
      }
    }

    "calls DisposeService invoke with the expected DisposeRequest" in new WithApplication {
      val disposeServiceMock = mock[DisposeService]
      when(disposeServiceMock.invoke(any[DisposeRequest])).thenReturn(Future{ (0,None) })
      val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
      val disposeController = new disposal_of_vehicle.Dispose(disposeServiceMock, dateServiceStubbed())(clientSideSessionFactory)

      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())

      val result = disposeController.submit(request)


      val disposeRequest = DisposeRequest(
        referenceNumber = ReferenceNumberValid,
        registrationNumber = RegistrationNumberValid,
        traderName = TraderBusinessNameValid,
        traderAddress = DisposalAddressDto(line = Seq(Line1Valid, Line2Valid, Line3Valid),postTown = Some(Line4Valid),postCode = PostcodeValid,uprn = None),
        dateOfDisposal = dateValid,
        transactionTimestamp = dateValid,
        prConsent = ConsentValid.toBoolean,
        keeperConsent = ConsentValid.toBoolean,
        trackingId = DefaultTrackingId,
        mileage = Some(MileageValid.toInt)
      )

      verify(disposeServiceMock, times(1)).invoke(cmd = disposeRequest)
    }

//    "Ensure the DisposeRequest has the tracking ID set" in new WithApplication {
//      val request = buildCorrectlyPopulatedRequest.
//        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
//        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
//        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel())
//      val mockDisposeService = mock[DisposeService]
//      when(mockDisposeService.invoke(any(classOf[DisposeRequest]))).thenReturn(Future[(Int, Option[DisposeResponse])] {
//        (200, None)
//      })
//      val invokeCaptor = ArgumentCaptor.forClass(classOf[DisposeRequest])
//      val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
//      val dispose = new disposal_of_vehicle.Dispose(mockDisposeService, dateServiceStubbed())(clientSideSessionFactory)
//      val result = dispose.submit(request)
//      whenReady(result) {
//        r =>
//          verify(mockDisposeService).invoke(invokeCaptor.capture())
//          invokeCaptor.getAllValues.size should equal(1)
//
//          invokeCaptor.getValue.trackingId should equal("x" * 20)
//      }
//    }

    "truncate address lines 1,2,3 and 4 up to max characters" in new WithApplication {
      val disposeServiceMock = mock[DisposeService]
      when(disposeServiceMock.invoke(any[DisposeRequest])).thenReturn(Future{ (0,None) })
      val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
      val disposeController = new disposal_of_vehicle.Dispose(disposeServiceMock, dateServiceStubbed())(clientSideSessionFactory)

      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel(line1 = "a" * LineMaxLength + 1, line2 = "b" * LineMaxLength + 1, line3 = "c" * LineMaxLength + 1, line4 = "d" * LineMaxLength + 1)) // line1 is longer than maximum

      val result = disposeController.submit(request)

      val disposeRequest = DisposeRequest(
        registrationNumber = RegistrationNumberValid,
        referenceNumber = ReferenceNumberValid,
        traderName = TraderBusinessNameValid,
        traderAddress = DisposalAddressDto(line = Seq("a" * LineMaxLength, "b" * LineMaxLength, "c" * LineMaxLength),postTown = Some("d" * LineMaxLength),postCode = PostcodeValid,uprn = None),
        dateOfDisposal = dateValid,
        transactionTimestamp = dateValid,
        prConsent = ConsentValid.toBoolean,
        keeperConsent = ConsentValid.toBoolean,
        trackingId = DefaultTrackingId,
        mileage = Some(MileageValid.toInt)
      )
      verify(disposeServiceMock, times(1)).invoke(cmd = disposeRequest)
    }

    "truncate post town up to max characters" in new WithApplication {
      val disposeServiceMock = mock[DisposeService]
      when(disposeServiceMock.invoke(any[DisposeRequest])).thenReturn(Future{ (0,None) })
      val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
      val disposeController = new disposal_of_vehicle.Dispose(disposeServiceMock, dateServiceStubbed())(clientSideSessionFactory)

      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel(line4 = "a" * LineMaxLength + 1)) // line1 is longer than maximum

      val result = disposeController.submit(request)

      val disposeRequest = DisposeRequest(
        registrationNumber = RegistrationNumberValid,
        referenceNumber = ReferenceNumberValid,
        traderName = TraderBusinessNameValid,
        traderAddress = DisposalAddressDto(line = Seq(Line1Valid, Line2Valid , Line3Valid),postTown = Some("a" * LineMaxLength),postCode = PostcodeValid,uprn = None),
        dateOfDisposal = dateValid,
        transactionTimestamp = dateValid,
        prConsent = ConsentValid.toBoolean,
        keeperConsent = ConsentValid.toBoolean,
        trackingId = DefaultTrackingId,
        mileage = Some(MileageValid.toInt)
      )
      verify(disposeServiceMock, times(1)).invoke(cmd = disposeRequest)
    }

    "remove spaces from postcode on submit" in new WithApplication {
      val disposeServiceMock = mock[DisposeService]
      when(disposeServiceMock.invoke(any[DisposeRequest])).thenReturn(Future{ (0,None) })
      val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
      val disposeController = new disposal_of_vehicle.Dispose(disposeServiceMock, dateServiceStubbed())(clientSideSessionFactory)

      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel(traderPostcode = "CM8 1QJ")) // postcode contains space

      val result = disposeController.submit(request)

      val disposeRequest = DisposeRequest(
        registrationNumber = RegistrationNumberValid,
        referenceNumber = ReferenceNumberValid,
        traderName = TraderBusinessNameValid,
        traderAddress = DisposalAddressDto(line = Seq(Line1Valid, Line2Valid, Line3Valid),postTown = Some(Line4Valid),postCode = PostcodeValid,uprn = None),
        dateOfDisposal = dateValid,
        transactionTimestamp = dateValid,
        prConsent = ConsentValid.toBoolean,
        keeperConsent = ConsentValid.toBoolean,
        trackingId = DefaultTrackingId,
        mileage = Some(MileageValid.toInt)
      )

      verify(disposeServiceMock, times(1)).invoke(cmd = disposeRequest)
    }

    "truncate building name or number and place remainder on line 2 when line 2 is empty" in new WithApplication {
      val disposeServiceMock = mock[DisposeService]
      when(disposeServiceMock.invoke(any[DisposeRequest])).thenReturn(Future{ (0,None) })
      val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
      val disposeController = new disposal_of_vehicle.Dispose(disposeServiceMock, dateServiceStubbed())(clientSideSessionFactory)

      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel(line1 = "a" * 40, line2 = "")) // line1 is longer than maximum

      val result = disposeController.submit(request)

      val disposeRequest = DisposeRequest(
        registrationNumber = RegistrationNumberValid,
        referenceNumber = ReferenceNumberValid,
        traderName = TraderBusinessNameValid,
        traderAddress = DisposalAddressDto(line = Seq("a" * 30, "a" * 10, Line3Valid),postTown = Some(Line4Valid),postCode = PostcodeValid,uprn = None),
        dateOfDisposal = dateValid,
        transactionTimestamp = dateValid,
        prConsent = ConsentValid.toBoolean,
        keeperConsent = ConsentValid.toBoolean,
        trackingId = DefaultTrackingId,
        mileage = Some(MileageValid.toInt)
      )
      verify(disposeServiceMock, times(1)).invoke(cmd = disposeRequest)
    }


    "truncate address line 2 and place remainder on line 3 when line 3 is empty" in new WithApplication {
      val disposeServiceMock = mock[DisposeService]
      when(disposeServiceMock.invoke(any[DisposeRequest])).thenReturn(Future{ (0,None) })
      val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
      val disposeController = new disposal_of_vehicle.Dispose(disposeServiceMock, dateServiceStubbed())(clientSideSessionFactory)

      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel(line2 = "b" * 40, line3 = "")) // line1 is longer than maximum

      val result = disposeController.submit(request)

      val disposeRequest = DisposeRequest(
        registrationNumber = RegistrationNumberValid,
        referenceNumber = ReferenceNumberValid,
        traderName = TraderBusinessNameValid,
        traderAddress = DisposalAddressDto(line = Seq(Line1Valid, "b" * 30, "b" * 10),postTown = Some(Line4Valid),postCode = PostcodeValid,uprn = None),
        dateOfDisposal = dateValid,
        transactionTimestamp = dateValid,
        prConsent = ConsentValid.toBoolean,
        keeperConsent = ConsentValid.toBoolean,
        trackingId = DefaultTrackingId,
        mileage = Some(MileageValid.toInt)
      )
      verify(disposeServiceMock, times(1)).invoke(cmd = disposeRequest)
    }

    "truncate building name or number and place remainder on line 2 when line 3 is empty. Line 2 is over max length, should be placed on line 3 and truncated" in new WithApplication {
      val disposeServiceMock = mock[DisposeService]
      when(disposeServiceMock.invoke(any[DisposeRequest])).thenReturn(Future{ (0,None) })
      val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
      val disposeController = new disposal_of_vehicle.Dispose(disposeServiceMock, dateServiceStubbed())(clientSideSessionFactory)

      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel(line1= "a" * 40, line2 = "b" * 40, line3 = ""))

      val result = disposeController.submit(request)

      val disposeRequest = DisposeRequest(
        registrationNumber = RegistrationNumberValid,
        referenceNumber = ReferenceNumberValid,
        traderName = TraderBusinessNameValid,
        traderAddress = DisposalAddressDto(line = Seq("a" * 30, "a" * 10, "b" * 30),postTown = Some(Line4Valid),postCode = PostcodeValid,uprn = None),
        dateOfDisposal = dateValid,
        transactionTimestamp = dateValid,
        prConsent = ConsentValid.toBoolean,
        keeperConsent = ConsentValid.toBoolean,
        trackingId = DefaultTrackingId,
        mileage = Some(MileageValid.toInt)
      )
      verify(disposeServiceMock, times(1)).invoke(cmd = disposeRequest)
    }

    "truncate building name or number when over 30 characters. Move line 2 to line 3 and remainder of building name or number to line 2 when line 3 is empty" in new WithApplication {
      val disposeServiceMock = mock[DisposeService]
      when(disposeServiceMock.invoke(any[DisposeRequest])).thenReturn(Future{ (0,None) })
      val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
      val disposeController = new disposal_of_vehicle.Dispose(disposeServiceMock, dateServiceStubbed())(clientSideSessionFactory)

      val request = buildCorrectlyPopulatedRequest.
        withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
        withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
        withCookies(CookieFactoryForUnitSpecs.traderDetailsModel(line1 = "c" * 40, line3 = "")) // line1 is longer than maximum

      val result = disposeController.submit(request)

      val disposeRequest = DisposeRequest(
        registrationNumber = RegistrationNumberValid,
        referenceNumber = ReferenceNumberValid,
        traderName = TraderBusinessNameValid,
        traderAddress = DisposalAddressDto(line = Seq("c" * 30, "c" * 10, Line2Valid),postTown = Some(Line4Valid),postCode = PostcodeValid,uprn = None),
        dateOfDisposal = dateValid,
        transactionTimestamp = dateValid,
        prConsent = ConsentValid.toBoolean,
        keeperConsent = ConsentValid.toBoolean,
        trackingId = DefaultTrackingId,
        mileage = Some(MileageValid.toInt)
      )
      verify(disposeServiceMock, times(1)).invoke(cmd = disposeRequest)
    }
  }

  "truncate building name or number, create line 2 and move reaminder to line2  when only building name or number, town and postcode returned" in new WithApplication {
    val disposeServiceMock = mock[DisposeService]
    when(disposeServiceMock.invoke(any[DisposeRequest])).thenReturn(Future{ (0,None) })
    val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
    val disposeController = new disposal_of_vehicle.Dispose(disposeServiceMock, dateServiceStubbed())(clientSideSessionFactory)

    val request = buildCorrectlyPopulatedRequest.
      withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
      withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
      withCookies(CookieFactoryForUnitSpecs.traderDetailsModelLine1(line1 = "a" * LineMaxLength + "a", line4 = Line4Valid)). // line1 is longer than maximum
      withCookies(CookieFactoryForUnitSpecs.trackingIdModel("x" * 20))

    val result = disposeController.submit(request)

    val disposeRequest = DisposeRequest(
      registrationNumber = RegistrationNumberValid,
      referenceNumber = ReferenceNumberValid,
      traderName = TraderBusinessNameValid,
      traderAddress = DisposalAddressDto(line = Seq("a" * LineMaxLength, "a"),postTown = Some(Line4Valid),postCode = PostcodeValid,uprn = None),
      dateOfDisposal = dateValid,
      transactionTimestamp = dateValid,
      prConsent = ConsentValid.toBoolean,
      keeperConsent = ConsentValid.toBoolean,
      trackingId = "x" * 20,
      mileage = Some(MileageValid.toInt)
    )
    verify(disposeServiceMock, times(1)).invoke(cmd = disposeRequest)
  }

  "truncate address line 2, create line 3 and move reaminder to line3 when only building name or number, line2, town and postcode returned" in new WithApplication {
    val disposeServiceMock = mock[DisposeService]
    when(disposeServiceMock.invoke(any[DisposeRequest])).thenReturn(Future{ (0,None) })
    val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
    val disposeController = new disposal_of_vehicle.Dispose(disposeServiceMock, dateServiceStubbed())(clientSideSessionFactory)

    val request = buildCorrectlyPopulatedRequest.
      withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
      withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
      withCookies(CookieFactoryForUnitSpecs.traderDetailsModelLine2(line2 = "b" * LineMaxLength + "b", line4 = Line4Valid)).
      withCookies(CookieFactoryForUnitSpecs.trackingIdModel("x" * 20))

    val result = disposeController.submit(request)

    val disposeRequest = DisposeRequest(
      registrationNumber = RegistrationNumberValid,
      referenceNumber = ReferenceNumberValid,
      traderName = TraderBusinessNameValid,
      traderAddress = DisposalAddressDto(line = Seq(Line1Valid, "b" * LineMaxLength, "b"),postTown = Some(Line4Valid),postCode = PostcodeValid,uprn = None),
      dateOfDisposal = dateValid,
      transactionTimestamp = dateValid,
      prConsent = ConsentValid.toBoolean,
      keeperConsent = ConsentValid.toBoolean,
      trackingId = "x" * 20,
      mileage = Some(MileageValid.toInt)
    )
    verify(disposeServiceMock, times(1)).invoke(cmd = disposeRequest)
  }

  "truncate building name or number, create line 3, move line 2 to line 3 remainder of building name or number to line 2 when building name or number, line2, town and postcode returned" in new WithApplication {
    val disposeServiceMock = mock[DisposeService]
    when(disposeServiceMock.invoke(any[DisposeRequest])).thenReturn(Future{ (0,None) })
    val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
    val disposeController = new disposal_of_vehicle.Dispose(disposeServiceMock, dateServiceStubbed())(clientSideSessionFactory)

    val request = buildCorrectlyPopulatedRequest.
      withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
      withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
      withCookies(CookieFactoryForUnitSpecs.traderDetailsModelLine2(line1 = "a" * LineMaxLength + "a", line2 = "b" * LineMaxLength + "b", line4 = Line4Valid)).
      withCookies(CookieFactoryForUnitSpecs.trackingIdModel("x" * 20))

    val result = disposeController.submit(request)

    val disposeRequest = DisposeRequest(
      registrationNumber = RegistrationNumberValid,
      referenceNumber = ReferenceNumberValid,
      traderName = TraderBusinessNameValid,
      traderAddress = DisposalAddressDto(line = Seq("a" * LineMaxLength, "a", "b" * LineMaxLength),postTown = Some(Line4Valid),postCode = PostcodeValid,uprn = None),
      dateOfDisposal = dateValid,
      transactionTimestamp = dateValid,
      prConsent = ConsentValid.toBoolean,
      keeperConsent = ConsentValid.toBoolean,
      trackingId = "x" * 20,
      mileage = Some(MileageValid.toInt)
    )
    verify(disposeServiceMock, times(1)).invoke(cmd = disposeRequest)
  }

  "create dummy building name or number when only town and postcode returned" in new WithApplication {
    val disposeServiceMock = mock[DisposeService]
    when(disposeServiceMock.invoke(any[DisposeRequest])).thenReturn(Future{ (0,None) })
    val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
    val disposeController = new disposal_of_vehicle.Dispose(disposeServiceMock, dateServiceStubbed())(clientSideSessionFactory)

    val request = buildCorrectlyPopulatedRequest.
      withCookies(CookieFactoryForUnitSpecs.vehicleLookupFormModel()).
      withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
      withCookies(CookieFactoryForUnitSpecs.traderDetailsModelLine4(line4 = Line4Valid)).
      withCookies(CookieFactoryForUnitSpecs.trackingIdModel("x" * 20))

    val result = disposeController.submit(request)

    val disposeRequest = DisposeRequest(
      registrationNumber = RegistrationNumberValid,
      referenceNumber = ReferenceNumberValid,
      traderName = TraderBusinessNameValid,
      traderAddress = DisposalAddressDto(line = Seq(BuildingNameOrNumberHolder),postTown = Some(Line4Valid),postCode = PostcodeValid,uprn = None),
      dateOfDisposal = dateValid,
      transactionTimestamp = dateValid,
      prConsent = ConsentValid.toBoolean,
      keeperConsent = ConsentValid.toBoolean,
      trackingId = "x" * 20,
      mileage = Some(MileageValid.toInt)
    )
    verify(disposeServiceMock, times(1)).invoke(cmd = disposeRequest)
  }

  private val dateValid: String = DayMonthYear(DateOfDisposalDayValid.toInt, DateOfDisposalMonthValid.toInt, DateOfDisposalYearValid.toInt).toDateTime.get.toString

  private val emptySpace = " "

  private def dateServiceStubbed(day: Int = DateOfDisposalDayValid.toInt,
                                 month: Int = DateOfDisposalMonthValid.toInt,
                                 year: Int = DateOfDisposalYearValid.toInt) = {
    val dateService = mock[DateService]
    when(dateService.today).thenReturn(new models.DayMonthYear(day = day,
      month = month,
      year = year))
    dateService
  }

  private def buildCorrectlyPopulatedRequest = {
    import mappings.common.DayMonthYear._
    FakeRequest().withFormUrlEncodedBody(
      MileageId -> MileageValid,
      s"$DateOfDisposalId.$DayId" -> DateOfDisposalDayValid,
      s"$DateOfDisposalId.$MonthId" -> DateOfDisposalMonthValid,
      s"$DateOfDisposalId.$YearId" -> DateOfDisposalYearValid,
      ConsentId -> ConsentValid,
      LossOfRegistrationConsentId -> ConsentValid
    )
  }

  private def disposeController(disposeServiceStatus: Int = OK,
                                disposeServiceResponse: Option[DisposeResponse] = Some(disposeResponseSuccess)) = {
    val ws = mock[DisposeWebService]
    when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
      val fakeJson = disposeServiceResponse map (Json.toJson(_))
      new FakeResponse(status = disposeServiceStatus, fakeJson = fakeJson) // Any call to a webservice will always return this successful response.
    })

    val disposeServiceImpl = new DisposeServiceImpl(new Config(), ws)
    val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
    new disposal_of_vehicle.Dispose(disposeServiceImpl, dateServiceStubbed())(clientSideSessionFactory)
  }


  private def buildCheckboxHtml(widgetName: String, checked: Boolean): String = {
    if (checked)
      s"""<inputtype="checkbox"id="$widgetName"name="$widgetName"value="true"checkedaria-required=true>"""
    else
      s"""<inputtype="checkbox"id="$widgetName"name="$widgetName"value="true"aria-required=true>"""
  }

  private def buildSelectedOptionHtml(optionValue: String, optionText: String): String = {
    s"""<optionvalue="$optionValue"selected>$optionText</option>"""
  }
}
