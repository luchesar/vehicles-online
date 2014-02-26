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
import org.mockito.Matchers._
import services.ordnance_survey.domain.{OSAddressbaseDPA, OSAddressbaseResult}

class AddressLookupServiceSpec extends WordSpec with Matchers with MockitoSugar {
  "AddressLookupService" should {
    class PartialMockAddressLookupService(ws: services.WebService = new FakeWebServiceImpl,
                                             response: Future[Response],
                                             results: Option[Seq[OSAddressbaseResult]]) extends AddressLookupServiceImpl(ws) {
      override protected def callWebService(postcode: String): Future[Response] = response

      override def extractFromJson(resp: Response): Option[Seq[OSAddressbaseResult]] = results
    }

    val oSAddressbaseResultsValid = {
      val oSAddressbaseDPA = mock[OSAddressbaseDPA]
      when(oSAddressbaseDPA.UPRN).thenReturn("123")
      when(oSAddressbaseDPA.address).thenReturn("stubbed address")

      val oSAddressbaseResult = mock[OSAddressbaseResult]
      when(oSAddressbaseResult.DPA).thenReturn(Some(oSAddressbaseDPA))

      Seq(oSAddressbaseResult, oSAddressbaseResult, oSAddressbaseResult)
    }


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
}
