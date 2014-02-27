package services

import org.scalatest.{Matchers, WordSpec}
import services.fakes.FakeWebServiceImpl
import org.scalatest.mock.MockitoSugar
import play.api.libs.ws.Response
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.ordnance_survey.AddressLookupServiceImpl
import helpers.disposal_of_vehicle.PostcodePage.postcodeValid
import org.mockito.Mockito._
import services.ordnance_survey.domain.{OSAddressbaseHeader, OSAddressbaseSearchResponse, OSAddressbaseDPA, OSAddressbaseResult}
import play.api.libs.json.Json
import java.net.URI

class AddressLookupServiceSpec extends WordSpec with Matchers with MockitoSugar {

  // This class allows overriding of the base classes methods which call the real web service.
  class PartialMockAddressLookupService(ws: services.WebService = new FakeWebServiceImpl,
                                        response: Future[Response],
                                        results: Option[Seq[OSAddressbaseResult]]) extends AddressLookupServiceImpl(ws) {
    override protected def callWebService(postcode: String): Future[Response] = response

    override def extractFromJson(resp: Response): Option[Seq[OSAddressbaseResult]] = results
  }

  val oSAddressbaseResultsValid = {
    val dpa = OSAddressbaseDPA(
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

    val oSAddressbaseResult = OSAddressbaseResult(DPA = Some(dpa), LPI = None)

    Seq(oSAddressbaseResult, oSAddressbaseResult, oSAddressbaseResult)
  }

  "fetchAddressesForPostcode" should {
    "return empty seq when response is Ok but results is empty" in {
      // Arrange
      val response = mock[Response]
      when(response.status).thenReturn(200)

      val addressLookupServiceImpl = new PartialMockAddressLookupService(
        response = Future {
          response
        },
        results = None)

      // Act
      val result = addressLookupServiceImpl.fetchAddressesForPostcode(postcodeValid)

      // Assert
      result.map { r =>
        r should equal(Seq.empty)
      }
    }

    "return seq when response status is 200" in {
      // Arrange
      val response = mock[Response]
      when(response.status).thenReturn(200)

      val addressLookupServiceImpl = new PartialMockAddressLookupService(
        response = Future {
          response
        },
        results = Some(oSAddressbaseResultsValid)
      )

      // Act
      val result = addressLookupServiceImpl.fetchAddressesForPostcode(postcodeValid)

      // Assert
      result.map { r =>
        r.length should equal(oSAddressbaseResultsValid.length)
        r should equal(oSAddressbaseResultsValid)
      }
    }

    "return empty seq when response status is not Ok (200)" in {
      // Arrange
      val response = mock[Response]
      when(response.status).thenReturn(404)

      val addressLookupServiceImpl = new PartialMockAddressLookupService(
        response = Future {
          response
        },
        results = Some(oSAddressbaseResultsValid)
      )

      // Act
      val result = addressLookupServiceImpl.fetchAddressesForPostcode(postcodeValid)

      // Assert
      result.map { r =>
        r should equal(Seq.empty)
      }
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

    val addressLookupServiceImpl = new AddressLookupServiceImpl(ws = new FakeWebServiceImpl)


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
      val result = addressLookupServiceImpl.extractFromJson(response)

      // Assert
      result should equal(expectedResults)
    }

    "return expected given valid json with results" in {
      // Arrange
      val expectedResults: Option[Seq[OSAddressbaseResult]] = Some(oSAddressbaseResultsValid)

      val inputAsJson = {
        val input = OSAddressbaseSearchResponse(
          header = oSAddressbaseHeader,
          results = expectedResults)
        Json.toJson(input)
      }

      val response = mock[Response] // It's very hard to create a Response so use a mock.
      when(response.json).thenReturn(inputAsJson)

      // Act
      val result = addressLookupServiceImpl.extractFromJson(response)

      // Assert
      result should equal(expectedResults)
    }

    "return empty list given invalid json" in {
      // Arrange
      val inputAsJson = Json.toJson("INVALID")


      val response = mock[Response] // It's very hard to create a Response so use a mock.
      when(response.json).thenReturn(inputAsJson)

      // Act

      val result = addressLookupServiceImpl.extractFromJson(response)

      // Assert
      result should equal(None)
    }
  }
}
