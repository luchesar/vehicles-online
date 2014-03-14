package services

import org.scalatest.{Matchers, WordSpec}
import services.fakes.FakeWebServiceImpl
import org.scalatest.mock.MockitoSugar
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.ordnance_survey.AddressLookupServiceImpl
import helpers.disposal_of_vehicle.Helper._
import org.mockito.Mockito._
import services.ordnance_survey.domain._
import play.api.libs.json.Json
import java.net.URI
import play.api.libs.ws.Response

class AddressLookupServiceSpec extends WordSpec with Matchers with MockitoSugar {

  // This class allows overriding of the base classes methods which call the real web service.
  class PartialMockAddressLookupService(ws: services.WebService = new FakeWebServiceImpl,
                                        responseOfPostcodeWebService: Future[Response] = Future {
                                          mock[Response]
                                        },
                                        responseOfUprnWebService: Future[Response] = Future {
                                          mock[Response]
                                        },
                                        results: Option[Seq[OSAddressbaseResult]]) extends AddressLookupServiceImpl(ws) {
    override protected def callPostcodeWebService(postcode: String): Future[Response] = responseOfPostcodeWebService

    override protected def callUprnWebService(uprn: String): Future[Response] = responseOfUprnWebService

    override def extractFromJson(resp: Response): Option[Seq[OSAddressbaseResult]] = results
  }

  val oSAddressbaseDPA = OSAddressbaseDPA(
    UPRN = "1",
    address = "a",
    postTown = "b",
    postCode = "c",
    RPC = "d",
    xCoordinate = 1f,
    yCoordinate = 2f,
    status = "e",
    matchScore = 3f,
    matchDescription = "f"
  )

  val oSAddressbaseResultsValidDPA = {
    val oSAddressbaseResult = OSAddressbaseResult(DPA = Some(oSAddressbaseDPA), LPI = None)

    Seq(oSAddressbaseResult, oSAddressbaseResult, oSAddressbaseResult)
  }

  val oSAddressbaseResultsEmptyDPAAndLPI = {
    val oSAddressbaseResult = OSAddressbaseResult(DPA = None, LPI = None)

    Seq(oSAddressbaseResult, oSAddressbaseResult, oSAddressbaseResult)
  }

  "fetchAddressesForPostcode" should {
    "return seq when response status is 200" in {
      // Arrange
      val response = mock[Response]
      when(response.status).thenReturn(200)

      val addressLookupService = new PartialMockAddressLookupService(
        responseOfPostcodeWebService = Future {
          response
        },
        results = Some(oSAddressbaseResultsValidDPA)
      )

      // Act
      val result = addressLookupService.fetchAddressesForPostcode(postcodeValid)

      // Assert
      result.map { r =>
        r.length should equal(oSAddressbaseResultsValidDPA.length)
        r should equal(oSAddressbaseResultsValidDPA)
      }
    }

    "return empty seq when response status is Ok but results is empty" in {
      // Arrange
      val response = mock[Response]
      when(response.status).thenReturn(200)

      val addressLookupService = new PartialMockAddressLookupService(
        responseOfPostcodeWebService = Future {
          response
        },
        results = None)

      // Act
      val result = addressLookupService.fetchAddressesForPostcode(postcodeValid)

      // Assert
      result.map { r =>
        r should equal(Seq.empty)
      }
    }

    "return empty seq when response status is not Ok (200)" in {
      // Arrange
      val response = mock[Response]
      when(response.status).thenReturn(404)

      val addressLookupService = new PartialMockAddressLookupService(
        responseOfPostcodeWebService = Future {
          response
        },
        results = Some(oSAddressbaseResultsValidDPA)
      )

      // Act
      val result = addressLookupService.fetchAddressesForPostcode(postcodeValid)

      // Assert
      result.map { r =>
        r should equal(Seq.empty)
      }
    }

    "return empty seq when the result has no DPA and no LPI" in {
      // Arrange
      val response = mock[Response]
      when(response.status).thenReturn(200)

      val addressLookupService = new PartialMockAddressLookupService(
        responseOfPostcodeWebService = Future {
          response
        },
        results = Some(oSAddressbaseResultsEmptyDPAAndLPI)
      )

      // Act
      val result = addressLookupService.fetchAddressesForPostcode(postcodeValid)

      // Assert
      result.map { r =>
        r should equal(Seq.empty)
      }
    }

    "return empty seq when web service throws an exception" in {
      // Arrange
      val response = mock[Response]
      when(response.status).thenThrow(new RuntimeException("This error is generated deliberately by a test"))

      val oSAddressbaseResultsEmptyDPAAndLPI = {
        val oSAddressbaseResult = OSAddressbaseResult(DPA = None, LPI = None)

        Seq(oSAddressbaseResult, oSAddressbaseResult, oSAddressbaseResult)
      }

      val addressLookupService = new PartialMockAddressLookupService(
        responseOfPostcodeWebService = Future {
          response
        },
        results = Some(oSAddressbaseResultsEmptyDPAAndLPI)
      )

      // Act
      val result = addressLookupService.fetchAddressesForPostcode(postcodeValid)

      // Assert
      result.map { r =>
        r should equal(Seq.empty)
      }
    }
  }

  "postcodeWithNoSpaces" should {
    val addressLookupService = new AddressLookupServiceImpl(ws = new FakeWebServiceImpl)

    "return the same string if no spaces present" in {
      val result = addressLookupService.postcodeWithNoSpaces(postcodeValid)

      result should equal(postcodeValid)
    }

    "remove spaces when present" in {
      val result = addressLookupService.postcodeWithNoSpaces(postcodeValidWithSpace)

      result should equal(postcodeValid)
    }
  }

  "extract from json" should {
    val oSAddressbaseHeader = {
      val uri = new URI("a")
      val query = "b"
      val offset = 2
      val totalresults = 1
      val format = "c"
      val dataset = "d"
      val maxresults = 3

      OSAddressbaseHeader(
        uri = uri,
        query = query,
        offset = offset,
        totalresults = totalresults,
        format = format,
        dataset = dataset,
        maxresults = maxresults
      )
    }

    val addressLookupService = new AddressLookupServiceImpl(ws = new FakeWebServiceImpl)


    "return expected given valid json with no results" in {
      // Arrange
      val expectedResults: Option[Seq[OSAddressbaseResult]] = None

      val inputAsJson = {
        val input = OSAddressbaseSearchResponse(
          header = oSAddressbaseHeader,
          results = expectedResults)
        Json.toJson(input)
      }

      val response = mock[Response] // It's very hard to create a Response so use a mock.
      when(response.json).thenReturn(inputAsJson)

      // Act
      val result = addressLookupService.extractFromJson(response)

      // Assert
      result should equal(expectedResults)
    }

    "return expected given valid json with results" in {
      // Arrange
      val expectedResults: Option[Seq[OSAddressbaseResult]] = Some(oSAddressbaseResultsValidDPA)

      val inputAsJson = {
        val input = OSAddressbaseSearchResponse(
          header = oSAddressbaseHeader,
          results = expectedResults)
        Json.toJson(input)
      }

      val response = mock[Response] // It's very hard to create a Response so use a mock.
      when(response.json).thenReturn(inputAsJson)

      // Act
      val result = addressLookupService.extractFromJson(response)

      // Assert
      result should equal(expectedResults)
    }

    "return empty list given invalid json" in {
      // Arrange
      val inputAsJson = Json.toJson("INVALID")


      val response = mock[Response] // It's very hard to create a Response so use a mock.
      when(response.json).thenReturn(inputAsJson)

      // Act

      val result = addressLookupService.extractFromJson(response)

      // Assert
      result should equal(None)
    }
  }

  "fetchAddressForUprn" should {
    "return AddressViewModel when response status is 200" in {
      // Arrange
      val response = mock[Response]
      when(response.status).thenReturn(200)

      val addressLookupService = new PartialMockAddressLookupService(
        responseOfUprnWebService = Future {
          response
        },
        results = Some(oSAddressbaseResultsValidDPA)
      )

      // Act
      val result = addressLookupService.fetchAddressForUprn(oSAddressbaseDPA.UPRN)

      // Assert
      result.map {
        case Some(addressViewModel) => {
          addressViewModel.uprn should equal(oSAddressbaseDPA.UPRN)
          addressViewModel.address should equal(oSAddressbaseDPA.address)
        }
        case _ => fail("Should have returned Some(AddressViewModel)")
      }
    }

    "return None when response status is not 200" in {
      // Arrange
      val response = mock[Response]
      when(response.status).thenReturn(404)

      val addressLookupService = new PartialMockAddressLookupService(
        responseOfUprnWebService = Future {
          response
        },
        results = Some(oSAddressbaseResultsValidDPA)
      )

      // Act
      val result = addressLookupService.fetchAddressForUprn(oSAddressbaseDPA.UPRN)

      // Assert
      result.map {
        case None =>
        case _ => fail("Should have returned Some(AddressViewModel)")
      }
    }

    "return none when response status is Ok but results is empty" in {
      // Arrange
      val response = mock[Response]
      when(response.status).thenReturn(200)

      val addressLookupService = new PartialMockAddressLookupService(
        responseOfUprnWebService = Future {
          response
        },
        results = None
      )

      // Act
      val result = addressLookupService.fetchAddressForUprn(oSAddressbaseDPA.UPRN)

      // Assert
      result.map {
        case None =>
        case _ => fail("Should have returned Some(AddressViewModel)")
      }
    }

    "return none when the result has no DPA and no LPI" in {
      // Arrange
      val response = mock[Response]
      when(response.status).thenReturn(200)

      val addressLookupService = new PartialMockAddressLookupService(
        responseOfUprnWebService = Future {
          response
        },
        results = Some(oSAddressbaseResultsEmptyDPAAndLPI)
      )

      // Act
      val result = addressLookupService.fetchAddressForUprn(oSAddressbaseDPA.UPRN)

      // Assert
      result.map {
        case None =>
        case _ => fail("Should have returned Some(AddressViewModel)")
      }
    }

    "return none when web service throws an exception" in {
      // Arrange
      val response = mock[Response]
      when(response.status).thenThrow(new RuntimeException("This error is generated deliberately by a test"))

      val addressLookupService = new PartialMockAddressLookupService(
        responseOfUprnWebService = Future {
          response
        },
        results = Some(oSAddressbaseResultsEmptyDPAAndLPI)
      )

      // Act
      val result = addressLookupService.fetchAddressForUprn(oSAddressbaseDPA.UPRN)

      // Assert
      result.map {
        case None =>
        case _ => fail("Should have returned Some(AddressViewModel)")
      }
    }
  }
}
