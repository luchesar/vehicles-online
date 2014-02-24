package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.VehicleLookup._
import helpers.disposal_of_vehicle.Helper._

class VehicleLookupFormSpec extends WordSpec with Matchers {
  "V5cSearch Form" should {

    def vehicleLookupFiller(v5cReferenceNumber: String, v5cRegistrationNumber: String, v5cKeeperName: String, v5cPostcode: String ) = {
      VehicleLookup.vehicleLookupForm.bind(
        Map(
          v5cReferenceNumberId -> v5cReferenceNumber,
          v5cRegistrationNumberId-> v5cRegistrationNumber,
          v5cKeeperNameId ->  v5cKeeperName,
          v5cPostcodeId -> v5cPostcode
        )
      )
    }
//v5cReferenceNumber tests
    "reject if v5cReferenceNumber is blank" in {
      vehicleLookupFiller(v5cReferenceNumber = "", v5cRegistrationNumber = v5cVehicleRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(3)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cReferenceNumber is less than min length" in {
      vehicleLookupFiller(v5cReferenceNumber = "1234567891", v5cRegistrationNumber = v5cVehicleRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cReferenceNumber is greater than max length" in {
      vehicleLookupFiller(v5cReferenceNumber = "123456789101", v5cRegistrationNumber = v5cVehicleRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cReferenceNumber contains letters" in {
      vehicleLookupFiller(v5cReferenceNumber = "qwertyuiopl", v5cRegistrationNumber = v5cVehicleRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cReferenceNumber contains special characters" in {
      vehicleLookupFiller(v5cReferenceNumber = "£££££££££££", v5cRegistrationNumber = v5cVehicleRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if v5cReferenceNumber is valid" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = v5cVehicleRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          fail("An error should occur")
        },
        f => f.v5cReferenceNumber should equal(v5cDocumentReferenceNumberValid)
      )
    }

//v5cRegistrationNumber tests
    "reject if v5cRegistrationNumber is empty" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cRegistrationNumber is less than min length" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "a", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cRegistrationNumber is more than max length" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "AB53 WERT", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cRegistrationNumber contains special characters" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "ab53ab%", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if v5cRegistrationNumber equals A 9" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "A 9", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("A 9")
      )
    }

    "accept if v5cRegistrationNumber equals 9 A" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "9 A", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("9 A")
      )
    }

    "accept if v5cRegistrationNumber equals AA 9" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "AA 9", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("AA 9")
      )
    }

    "accept if v5cRegistrationNumber equals A 99" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "A 99", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("A 99")
      )
    }

    "accept if v5cRegistrationNumber equals 9 AA" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "9 AA", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("9 AA")
      )
    }

    "accept if v5cRegistrationNumber equals 99 A" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "99 A", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("99 A")
      )
    }

    "accept if v5cRegistrationNumber equals AAA 9" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "AAA 9", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("AAA 9")
      )
    }

    "accept if v5cRegistrationNumber equals A 999" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "A 999", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("A 999")
      )
    }

    "accept if v5cRegistrationNumber equals AA 99" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "AA 99", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("AA 99")
      )
    }

    "accept if v5cRegistrationNumber equals 9 AAA" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "9 AAA", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("9 AAA")
      )
    }

    "accept if v5cRegistrationNumber equals 99 AA" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "99 AA", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("99 AA")
      )
    }

    "accept if v5cRegistrationNumber equals 999 A" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "999 A", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("999 A")
      )
    }

    "accept if v5cRegistrationNumber equals A9 AAA" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "A9 AAA", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("A9 AAA")
      )
    }

    "accept if v5cRegistrationNumber equals AAA 9A" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "AAA 9A", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("AAA 9A")
      )
    }

    "accept if v5cRegistrationNumber equals AAA 99" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "AAA 99", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("AAA 99")
      )
    }

    "accept if v5cRegistrationNumber equals AA 999" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "AA 999", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("AA 999")
      )
    }

    "accept if v5cRegistrationNumber equals 99 AAA" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "99 AAA", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("99 AAA")
      )
    }

    "accept if v5cRegistrationNumber equals 999 AA" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "999 AA", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("999 AA")
      )
    }

    "accept if v5cRegistrationNumber equals 9999 A" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "9999 A", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("9999 A")
      )
    }

    "accept if v5cRegistrationNumber equals A 9999" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "A 9999", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("A 9999")
      )
    }

    "accept if v5cRegistrationNumber equals A99 AAA" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "A99 AAA", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("A99 AAA")
      )
    }

    "accept if v5cRegistrationNumber equals AAA 99A" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "AAA 99A", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("AAA 99A")
      )
    }

    "accept if v5cRegistrationNumber equals AAA 999" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "AAA 999", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("AAA 999")
      )
    }

    "accept if v5cRegistrationNumber equals 999 AAA" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "999 AAA", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("999 AAA")
      )
    }

    "accept if v5cRegistrationNumber equals AA 9999" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "AA 9999", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("AA 9999")
      )
    }

    "accept if v5cRegistrationNumber equals 9999 AA" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "9999 AA", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("9999 AA")
      )
    }

    "accept if v5cRegistrationNumber equals A999 AAA" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "A999 AAA", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("A999 AAA")
      )
    }

    "accept if v5cRegistrationNumber equals AAA 999A" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "AAA 999A", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("AAA 999A")
      )
    }

    "accept if v5cRegistrationNumber equals AAA 9999" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "AAA 9999", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("AAA 9999")
      )
    }

    "accept if v5cRegistrationNumber equals AA99 AAA" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "AA99 AAA", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("AA99 AAA")
      )
    }

    "accept if v5cRegistrationNumber equals 9999 AAA" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "9999 AAA", v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.v5cRegistrationNumber should equal("9999 AAA")
      )
    }

    //v5cKeeperName tests
    "reject if v5cKeeperName is empty" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = v5cVehicleRegistrationNumberValid, v5cKeeperName = "", v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cKeeperName is more than max length" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = v5cVehicleRegistrationNumberValid, v5cKeeperName = "qwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopqwertyuiopq", v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if v5cKeeperName is valid" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = v5cVehicleRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          fail("An error should occur")
        },
        f => f.v5cKeeperName should equal(v5cKeeperNameValid)
      )
    }

//v5cPostcode tests
    "reject if v5cPostcode is empty" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = v5cVehicleRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(3)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cPostcode is less than min length" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = v5cVehicleRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = "sa91").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cPostcode is more than max length" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = v5cVehicleRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = "sa991bdsp").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur")
      )
    }

    "reject if v5cPostcode contains special characters" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = v5cVehicleRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = "sa991d£").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if v5cPostcode is valid" in {
      vehicleLookupFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = v5cVehicleRegistrationNumberValid, v5cKeeperName = v5cKeeperNameValid, v5cPostcode = v5cPostcodeValid).fold(
        formWithErrors => {
          fail("An error should occur")
        },
        f => f.v5cPostcode should equal(v5cPostcodeValid)
      )
    }
  }
}