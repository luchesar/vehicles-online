package services

import org.scalatest.{Matchers, WordSpec}
import services.fakes.FakeWebServiceImpl
import org.scalatest.mock.MockitoSugar
import play.api.libs.ws.Response
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import services.ordnance_survey.AddressLookupServiceImpl
import models.domain.disposal_of_vehicle.AddressViewModel
import helpers.disposal_of_vehicle.PostcodePage.postcodeValid
import org.mockito.Mockito._
import services.ordnance_survey.domain.OSAddressbaseResult

class AddressLookupServiceSpec extends WordSpec with Matchers with MockitoSugar {
  "AddressLookupService" should {
    class FakeInjectableAddressLookupService(ws: services.WebService, response: Future[Response], results: Option[Seq[OSAddressbaseResult]]) extends AddressLookupServiceImpl(ws) {
      val address1 = AddressViewModel(uprn = Some(1234L), address = Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
      val address2 = AddressViewModel(uprn = Some(4567L), address = Seq("Penarth Road", "Cardiff", "CF11 8TT"))

      override protected def lookup(postcode: String): Future[Response] = response

      override def fetchResults(resp: Response): Option[Seq[OSAddressbaseResult]] = results

      /*
      override def fetchAddressesForPostcode(postcode: String): Future[Seq[(String, String)]] = Future {
        if (postcode == postcodeInvalid) {
          Seq.empty
        }
        else {
          Seq(
            address1.uprn.getOrElse(1234L).toString -> address1.address.mkString(", "),
            address2.uprn.getOrElse(4567L).toString -> address2.address.mkString(", ")
          )
        }
      }

      override def fetchAddressForUprn(uprn: String): Future[Option[AddressViewModel]] = Future {
        Some(address1)
      }*/
    }

    "return empty seq when response is Ok but results is empty" in {
      // Arrange
      val webServiceImpl = new FakeWebServiceImpl
      val response = mock[Response]
      when(response.status).thenReturn(200)
      val addressLookupServiceImpl = new FakeInjectableAddressLookupService(
        ws = webServiceImpl,
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
      // TODO check step-by-step that this test actually exercises the production code that checks the response status.
      // Arrange
      val webServiceImpl = new FakeWebServiceImpl
      val response = mock[Response]
      when(response.status).thenReturn(200)
      val oSAddressbaseResult = mock[OSAddressbaseResult]
      val oSAddressbaseResults = Seq(oSAddressbaseResult, oSAddressbaseResult, oSAddressbaseResult)
      val addressLookupServiceImpl = new FakeInjectableAddressLookupService(
        ws = webServiceImpl,
        response = Future {
          response
        },
        results = Some(oSAddressbaseResults)
      )

      // Act
      val result = addressLookupServiceImpl.fetchAddressesForPostcode(postcodeValid)

      // Assert
      result.map { r =>
        r.length should equal(oSAddressbaseResults.length)
      }
    }

    //
    //"return empty seq  when response status is not Ok (200)"
  }
}
