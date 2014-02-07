package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import app.DisposalOfVehicle._

class VehicleLookupFormSpec extends WordSpec with Matchers {
  "V5cSearch Form" should {

    val v5cReferenceNumberValid = "12345678910"
    val v5cRegistrationNumberValid = "a1"
    val v5cKeeperNameValid = "John Snow"
    val v5cPostcodeValid = "SA44DW"

    def vehicleLookupFiller(v5cReferenceNumber: String, v5cRegistrationNumber: String, v5cKeeperName: String, v5cPostcode: String ) = {
      VehicleLookup.vehicleLookupForm.bind(
        Map(
          v5cReferenceNumberID -> v5cReferenceNumber,
          v5cRegistrationNumberID-> v5cRegistrationNumber,
          v5cKeeperNameID ->  v5cKeeperName,
          v5cPostcodeID -> v5cPostcode
        )
      )
    }

    "reject if v5cReferenceNumber is blank" in {
      vehicleLookupFiller(v5cReferenceNumber = "", v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          println(formWithErrors.errors)
          formWithErrors.errors.length should equal(3)
        },
        f => "An error should occur" should equal("Valid")
      )
    }

    "reject if v5cReferenceNumber is less than min length" in {
      pending
    }

    "reject if v5cReferenceNumber is greater than max length" in {
      pending
    }

    "reject if v5cReferenceNumber contains letters" in {
      pending
    }

    "reject if v5cReferenceNumber contains special characters" in {
      pending
    }

    "accept if all values contain valid entries" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f =>
          f.v5cReferenceNumber should equal(v5cReferenceNumberValid)
      )
    }
  }
}