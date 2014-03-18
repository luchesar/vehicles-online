package controllers.change_of_address

import mappings.change_of_address.V5cSearch
import V5cSearch._
import org.scalatest.mock.MockitoSugar
import helpers.change_of_address.Helper._
import helpers.UnitSpec
import helpers.UnitSpec

class V5cSearchFormSpec extends UnitSpec {

  "V5cSearch Form" should {
    val mockWebService = mock[services.V5cSearchWebService]
    val vehicleSearch = new VehicleSearch(mockWebService)

    def formWithValidDefaults(v5cReferenceNumber: String = v5cDocumentReferenceNumberValid,
                              v5cRegistrationNumber: String = v5cVehicleRegistrationNumberValid) = {
      vehicleSearch.vehicleSearchForm.bind(
        Map(
          v5cReferenceNumberId -> v5cReferenceNumber,
          v5cRegistrationNumberId -> v5cRegistrationNumber
        )
      )
    }

    "reject if referenceNumber is blank" in {
      formWithValidDefaults(v5cReferenceNumber = "").errors should have length 3
    }

    "reject if referenceNumber is less than minimum" in {
      formWithValidDefaults(v5cReferenceNumber = "1").errors should have length 1
    }

    "reject if referenceNumber is more than maximum" in {
      formWithValidDefaults(v5cReferenceNumber = "123456789101").errors should have length 1
    }

    "reject if referenceNumber contains letters" in {
      formWithValidDefaults(v5cReferenceNumber = "1234567891l").errors should have length 1
    }

    "reject if referenceNumber contains special charapostcodecters" in {
      formWithValidDefaults(v5cReferenceNumber = "1234567891%").errors should have length 1
    }

    "accept if referenceNumber contains valid input" in {
      formWithValidDefaults(v5cReferenceNumber = v5cDocumentReferenceNumberValid).
        get.v5cReferenceNumber should equal(v5cDocumentReferenceNumberValid)
    }

    "reject if vehicleVRN is blank" in {
      formWithValidDefaults(v5cRegistrationNumber = "").errors should have length 2
    }

    "reject if vehicleVRN is less than minimum" in {
      formWithValidDefaults(v5cRegistrationNumber = "a").errors should have length 2
    }

    "reject if vehicleVRN is more than maximum" in {
      formWithValidDefaults(v5cRegistrationNumber = "AB55CMWE").errors should have length 2
    }
    "reject if vehicleVRN contains special characters" in {
      formWithValidDefaults(v5cRegistrationNumber = "%^").errors should have length 1
    }

    "accept if vehicleVRN contains valid input" in {
      formWithValidDefaults(v5cRegistrationNumber = v5cVehicleRegistrationNumberValid).
        get.v5cRegistrationNumber should equal(v5cVehicleRegistrationNumberValid)
    }
  }
}