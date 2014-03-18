package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import mappings.change_of_address.V5cSearch
import V5cSearch._
import helpers.change_of_address.LoginCachePopulate
import LoginCachePopulate._
import org.mockito.Mockito._
import org.mockito.Matchers._
import models.domain.change_of_address.V5cSearchModel
import helpers.change_of_address.Helper._
import helpers.change_of_address.{AreYouRegisteredPage, ConfirmVehicleDetailsPage}
import services.fakes.FakeV5cSearchWebService
import helpers.UnitSpec

class V5cSearchUnitSpec extends UnitSpec {

  "V5cSearch - Controller" should {
    val mockV5cSearchModel = mock[V5cSearchModel]
    val mockWebService = mock[services.V5cSearchWebService]
    when(mockWebService.invoke(any[V5cSearchModel])).thenReturn(new FakeV5cSearchWebService().invoke(mockV5cSearchModel))
    val vehicleSearch = new VehicleSearch(mockWebService)

    "present when user has logged in" in new WithApplication {
      setupCache
      val request = FakeRequest().withSession()

      val result = vehicleSearch.present(request)

      status(result) should equal(OK)
    }

    "present login page when user is not logged in" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = vehicleSearch.present(request)

      redirectLocation(result) should equal(Some(AreYouRegisteredPage.url))
    }

    "redirect to next page after the button is clicked" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(v5cReferenceNumberId -> v5cDocumentReferenceNumberValid,
                                v5cRegistrationNumberId -> v5cVehicleRegistrationNumberValid)

      val result = vehicleSearch.submit(request)

      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal(Some(ConfirmVehicleDetailsPage.url))
    }

    "report bad request when no details are entered" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      val result = vehicleSearch.submit(request)

      status(result) should equal(BAD_REQUEST)
    }
  }
}