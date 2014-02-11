package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import app.DisposalOfVehicle.VehicleLookup._

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
//v5cReferenceNumber tests
    "reject if v5cReferenceNumber is blank" in {
      vehicleLookupFiller(v5cReferenceNumber = "", v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(3)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cReferenceNumber is less than min length" in {
      vehicleLookupFiller(v5cReferenceNumber = "1234567891", v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cReferenceNumber is greater than max length" in {
      vehicleLookupFiller(v5cReferenceNumber = "123456789101", v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cReferenceNumber contains letters" in {
      vehicleLookupFiller(v5cReferenceNumber = "qwertyuiopl", v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cReferenceNumber contains special characters" in {
      vehicleLookupFiller(v5cReferenceNumber = "£££££££££££", v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if v5cReferenceNumber is valid" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          fail("An error should occur")
        },
        f => f.v5cReferenceNumber should equal(v5cReferenceNumberValid)
      )
    }

//v5cRegistrationNumber tests
    "reject if v5cRegistrationNumber is empty" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cRegistrationNumber is less than min length" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "a", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cRegistrationNumber is more than max length" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "AB53 WERT", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cRegistrationNumber contains special characters" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = "ab53ab%", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if v5cRegistrationNumber is valid" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal(v5cRegistrationNumberValid)
      )
    }

//v5cKeeperName tests
    "reject if v5cKeeperName is empty" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = "", v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cKeeperName is more than max length" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopq", v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if v5cKeeperName is valid" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          fail("An error should occur")
        },
        f => f.v5cKeeperName should equal(v5cKeeperNameValid)
      )
    }

//v5cPostcode tests
    "reject if v5cPostcode is empty" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(3)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cPostcode is less than min length" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = "sa91").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cPostcode is more than max length" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = "sa991bdsp").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cPostcode contains special characters" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = "sa991d£").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if v5cPostcode is valid" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cReferenceNumberValid, v5cRegistrationNumber = v5cRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          fail("An error should occur")
        },
        f => f.v5cPostcode should equal(v5cPostcodeValid)
      )
    }
  }
}