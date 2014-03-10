package controllers.change_of_address

import org.scalatest.{Matchers, WordSpec}
import mappings.change_of_address.V5cSearch
import V5cSearch._
import org.scalatest.mock.MockitoSugar
import helpers.change_of_address.V5cSearchPagePopulate._
import helpers.change_of_address.Helper._

class V5cSearchFormSpec extends WordSpec with Matchers with MockitoSugar {
  "V5cSearch Form" should {
    val mockWebService = mock[services.V5cSearchWebService]
    val vehicleSearch = new VehicleSearch(mockWebService)

    def v5cSearchFiller(v5cReferenceNumber: String,v5cRegistrationNumber: String) = {
      vehicleSearch.vehicleSearchForm.bind(
        Map(
          v5cReferenceNumberId -> v5cReferenceNumber,
          v5cRegistrationNumberId-> v5cRegistrationNumber
        )
      )
    }

    /*Test referenceNumber*/
    "reject if referenceNumber is blank" in {
      v5cSearchFiller(v5cReferenceNumber = "", v5cRegistrationNumber = v5cVehicleRegistrationNumberValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(3)
        },
        f => fail("An error should occur")
      )
    }

    "reject if referenceNumber is less than minimum" in {
      v5cSearchFiller(v5cReferenceNumber = "1", v5cRegistrationNumber = v5cVehicleRegistrationNumberValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if referenceNumber is more than maximum" in {
      v5cSearchFiller(v5cReferenceNumber = "123456789101", v5cRegistrationNumber = v5cVehicleRegistrationNumberValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if referenceNumber contains letters" in {
      v5cSearchFiller(v5cReferenceNumber = "1234567891l", v5cRegistrationNumber = v5cVehicleRegistrationNumberValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if referenceNumber contains special charapostcodecters" in {
      v5cSearchFiller(v5cReferenceNumber = "1234567891%", v5cRegistrationNumber = v5cVehicleRegistrationNumberValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if referenceNumber contains valid input" in {
      v5cSearchFiller(v5cReferenceNumber=v5cDocumentReferenceNumberValid,v5cRegistrationNumber=v5cVehicleRegistrationNumberValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f =>
          f.v5cReferenceNumber should equal(v5cDocumentReferenceNumberValid)
      )
    }

    /*Test VRN*/
    "reject if vehicleVRN is blank" in {
      v5cSearchFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur")
      )
    }

    "reject if vehicleVRN is less than minimun" in {
      v5cSearchFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "a").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur")
      )
    }

    "reject if vehicleVRN is more than maximum" in {
      v5cSearchFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "AB55CMWE").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur")
      )
    }
    "reject if vehicleVRN contains special characters" in {
      v5cSearchFiller(v5cReferenceNumber = v5cDocumentReferenceNumberValid, v5cRegistrationNumber = "%^").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if vehicleVRN contains valid input" in {
      v5cSearchFiller(v5cReferenceNumber=v5cDocumentReferenceNumberValid,v5cRegistrationNumber=v5cVehicleRegistrationNumberValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f =>
          f.v5cRegistrationNumber should equal(v5cVehicleRegistrationNumberValid)
      )
    }
  }
}