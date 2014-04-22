package controllers.disposal_of_vehicle

import mappings.disposal_of_vehicle.VehicleLookup._
import org.mockito.Mockito._
import org.mockito.Matchers._
import models.domain.disposal_of_vehicle._
import services.fakes.FakeResponse
import controllers.disposal_of_vehicle
import helpers.UnitSpec
import services.vehicle_lookup.{VehicleLookupServiceImpl, VehicleLookupWebService}
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.Json
import ExecutionContext.Implicits.global
import services.fakes.FakeVehicleLookupWebService._
import helpers.disposal_of_vehicle.InvalidVRMFormat._
import helpers.disposal_of_vehicle.ValidVRMFormat._

class VehicleLookupFormSpec extends UnitSpec {

  "form" should {
    "accept when all fields contain valid responses" in {
      validVehicleLookupForm().get.referenceNumber should equal(referenceNumberValid)
      validVehicleLookupForm().get.registrationNumber should equal(registrationNumberValid)
    }
  }

  "referenceNumber" should {
    allInvalidVrmFormats.map(vrm => "reject invalid vehicle registration mark : " + vrm in {
      validVehicleLookupForm(registrationNumber = vrm).errors should have length 1
    })

    allValidVrmFormats.map(vrm => "accept valid vehicle registration mark : " + vrm in {
      validVehicleLookupForm(registrationNumber = vrm).get.registrationNumber should equal(vrm)
    })

    "reject if blank" in {
      val vehicleLookupFormError = validVehicleLookupForm(referenceNumber = "").errors
      val expectedKey = referenceNumberId

      vehicleLookupFormError should have length 3
      vehicleLookupFormError(0).key should equal(expectedKey)
      vehicleLookupFormError(0).message should equal("error.minLength")
      vehicleLookupFormError(1).key should equal(expectedKey)
      vehicleLookupFormError(1).message should equal("error.required")
      vehicleLookupFormError(2).key should equal(expectedKey)
      vehicleLookupFormError(2).message should equal("error.restricted.validNumberOnly")
    }

    "reject if less than min length" in {
      validVehicleLookupForm(referenceNumber = "1234567891").errors should have length 1
    }

    "reject if greater than max length" in {
      validVehicleLookupForm(referenceNumber = "123456789101").errors should have length 1
    }

    "reject if contains letters" in {
      validVehicleLookupForm(referenceNumber = "qwertyuiopl").errors should have length 1
    }

    "reject if contains special characters" in {
      validVehicleLookupForm(referenceNumber = "£££££££££££").errors should have length 1
    }

    "accept if valid" in {
      validVehicleLookupForm(registrationNumber = registrationNumberValid).get.referenceNumber should equal(referenceNumberValid)
    }
  }

  "registrationNumber" should {
    "reject if empty" in {
      validVehicleLookupForm(registrationNumber = "").errors should have length 3
    }

    "reject if less than min length" in {
      validVehicleLookupForm(registrationNumber = "a").errors should have length 2
    }

    "reject if more than max length" in {
      validVehicleLookupForm(registrationNumber = "AB53WERT").errors should have length 1
    }

    "reject if more than max length 2" in {
      validVehicleLookupForm(registrationNumber = "PJ056YYY").errors should have length 1
    }

    "reject if contains special characters" in {
      validVehicleLookupForm(registrationNumber = "ab53ab%").errors should have length 1
    }
  }

  private val vehicleLookup = {
    val ws: VehicleLookupWebService = mock[VehicleLookupWebService]
    when(ws.callVehicleLookupService(any[VehicleDetailsRequest])).thenReturn(Future {
      val responseAsJson = Json.toJson(vehicleDetailsResponseSuccess)
      import play.api.http.Status.OK
      new FakeResponse(status = OK, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
    })
    val vehicleLookupServiceImpl = new VehicleLookupServiceImpl(ws)
    new disposal_of_vehicle.VehicleLookup(vehicleLookupServiceImpl)
  }

  private def validVehicleLookupForm(referenceNumber: String = referenceNumberValid,
                                    registrationNumber: String = registrationNumberValid,
                                    consent: String = consentValid) = {
    vehicleLookup.vehicleLookupForm.bind(
      Map(
        referenceNumberId -> referenceNumber,
        registrationNumberId -> registrationNumber
      )
    )
  }


}