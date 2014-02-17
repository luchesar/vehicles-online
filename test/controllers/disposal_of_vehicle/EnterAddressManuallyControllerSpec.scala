package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.Dispose._
import org.specs2.mock.Mockito
import helpers.disposal_of_vehicle.{DisposeConfirmationPage, BusinessChooseYourAddressPage, SetUpTradeDetailsPage, VehicleLookupPage}
import helpers.disposal_of_vehicle.Helper._
import mappings.common.{PostCode, MultiLineAddress}
import helpers.disposal_of_vehicle.EnterAddressManuallyPopulate._

class EnterAddressManuallyControllerSpec extends WordSpec with Matchers with Mockito {
  "EnterAddressManually - Controller" should {

    "present" in new WithApplication {
      // Arrange
      SetUpTradeDetailsPage.setupCache
      val request = FakeRequest().withSession()

      // Act
      val result = disposal_of_vehicle.EnterAddressManually.present(request)

      // Assert
      status(result) should equal(OK)
    }

    "redirect to next page after a valid submit" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(
          s"${MultiLineAddress.id}.${MultiLineAddress.lineOneId}" -> line1Valid,
          s"${MultiLineAddress.id}.${MultiLineAddress.lineTwoId}" -> line2Valid,
          s"${MultiLineAddress.id}.${MultiLineAddress.lineThreeId}" -> line3Valid,
          PostCode.key -> postCodeValid
        )

      // Act
      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal (Some(VehicleLookupPage.url))
    }

    "return a bad request after an invalid submission" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody() // Empty form

      // Act
      val result = disposal_of_vehicle.EnterAddressManually.submit(request)

      // Assert
      status(result) should equal(BAD_REQUEST)
    }
  }
}