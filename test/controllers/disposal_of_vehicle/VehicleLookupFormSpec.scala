package controllers.disposal_of_vehicle

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import controllers.disposal_of_vehicle
import helpers.UnitSpec
import helpers.disposal_of_vehicle.InvalidVRMFormat._
import helpers.disposal_of_vehicle.ValidVRMFormat._
import mappings.disposal_of_vehicle.VehicleLookup._
import models.domain.disposal_of_vehicle._
import org.mockito.Matchers._
import org.mockito.Mockito._
import play.api.libs.json.{JsValue, Json}
import services.fakes.FakeResponse
import services.fakes.FakeVehicleLookupWebService._
import services.vehicle_lookup.{VehicleLookupServiceImpl, VehicleLookupWebService}
import utils.helpers.{CookieNameHashing, NoHash, CookieEncryption, NoEncryption}

final class VehicleLookupFormSpec extends UnitSpec {
  "form" should {
    "accept when all fields contain valid responses" in {
      formWithValidDefaults().get.referenceNumber should equal(referenceNumberValid)
      formWithValidDefaults().get.registrationNumber should equal(registrationNumberValid)
    }
  }

  "referenceNumber" should {
    allInvalidVrmFormats.map(vrm => "reject invalid vehicle registration mark : " + vrm in {
      formWithValidDefaults(registrationNumber = vrm).errors should have length 1
    })

    allValidVrmFormats.map(vrm => "accept valid vehicle registration mark : " + vrm in {
      formWithValidDefaults(registrationNumber = vrm).get.registrationNumber should equal(vrm)
    })

    "reject if blank" in {
      val vehicleLookupFormError = formWithValidDefaults(referenceNumber = "").errors
      val expectedKey = ReferenceNumberId
      
      vehicleLookupFormError should have length 3
      vehicleLookupFormError(0).key should equal(expectedKey)
      vehicleLookupFormError(0).message should equal("error.minLength")
      vehicleLookupFormError(1).key should equal(expectedKey)
      vehicleLookupFormError(1).message should equal("error.required")
      vehicleLookupFormError(2).key should equal(expectedKey)
      vehicleLookupFormError(2).message should equal("error.restricted.validNumberOnly")
    }

    "reject if less than min length" in {
      formWithValidDefaults(referenceNumber = "1234567891").errors should have length 1
    }

    "reject if greater than max length" in {
      formWithValidDefaults(referenceNumber = "123456789101").errors should have length 1
    }

    "reject if contains letters" in {
      formWithValidDefaults(referenceNumber = "qwertyuiopl").errors should have length 1
    }

    "reject if contains special characters" in {
      formWithValidDefaults(referenceNumber = "£££££££££££").errors should have length 1
    }

    "accept if valid" in {
      formWithValidDefaults(registrationNumber = registrationNumberValid).get.referenceNumber should equal(referenceNumberValid)
    }
  }

  "registrationNumber" should {
    "reject if empty" in {
      formWithValidDefaults(registrationNumber = "").errors should have length 3
    }

    "reject if less than min length" in {
      formWithValidDefaults(registrationNumber = "a").errors should have length 2
    }

    "reject if more than max length" in {
      formWithValidDefaults(registrationNumber = "AB53WERT").errors should have length 1
    }

    "reject if more than max length 2" in {
      formWithValidDefaults(registrationNumber = "PJ056YYY").errors should have length 1
    }

    "reject if contains special characters" in {
      formWithValidDefaults(registrationNumber = "ab53ab%").errors should have length 1
    }
  }

  private def vehicleLookupResponseGenerator(fullResponse:(Int, Option[VehicleDetailsResponse])) = {
    val ws: VehicleLookupWebService = mock[VehicleLookupWebService]
    when(ws.callVehicleLookupService(any[VehicleDetailsRequest])).thenReturn(Future {
      val responseAsJson : Option[JsValue] = fullResponse._2 match {
        case Some(e) => Some(Json.toJson(e))
        case _ => None
      }
      new FakeResponse(status = fullResponse._1, fakeJson = responseAsJson)// Any call to a webservice will always return this successful response.
    })
    val vehicleLookupServiceImpl = new VehicleLookupServiceImpl(ws)
    val noCookieEncryption = new NoEncryption with CookieEncryption
    val noCookieNameHashing = new NoHash with CookieNameHashing
    new disposal_of_vehicle.VehicleLookup(vehicleLookupServiceImpl)(noCookieEncryption, noCookieNameHashing)
  }

  private def formWithValidDefaults(referenceNumber: String = referenceNumberValid,
                                    registrationNumber: String = registrationNumberValid,
                                    consent: String = consentValid) = {
    vehicleLookupResponseGenerator(vehicleDetailsResponseSuccess).vehicleLookupForm.bind(
      Map(
        ReferenceNumberId -> referenceNumber,
        RegistrationNumberId -> registrationNumber
      )
    )
  }

}
