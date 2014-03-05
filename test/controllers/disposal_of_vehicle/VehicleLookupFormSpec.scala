package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.VehicleLookup._
import helpers.disposal_of_vehicle.Helper._
import org.mockito.Mockito._
import org.mockito.Matchers._
import models.domain.disposal_of_vehicle.VehicleLookupFormModel
import services.fakes.FakeVehicleLookupService
import controllers.disposal_of_vehicle
import org.scalatest.mock.MockitoSugar

class VehicleLookupFormSpec extends WordSpec with Matchers with MockitoSugar{
  "Vehicle lookup form" should {
    val mockVehicleLookupFormModel = mock[VehicleLookupFormModel]
    val mockWebService = mock[services.VehicleLookupService]
    when(mockWebService.invoke(any[VehicleLookupFormModel])).thenReturn(new FakeVehicleLookupService().invoke(mockVehicleLookupFormModel))
    val vehicleLookup = new disposal_of_vehicle.VehicleLookup(mockWebService)

    def vehicleLookupFiller(referenceNumber: String, registrationNumber: String, consent: String) = {
      vehicleLookup.vehicleLookupForm.bind(
        Map(
          referenceNumberId -> referenceNumber,
          registrationNumberId -> registrationNumber,
          consentId -> consent
        )
      )
    }

    /***********************
     * referenceNumber tests
     */
    "reject if referenceNumber is blank" in {
      vehicleLookupFiller(referenceNumber = "", registrationNumber = vehicleRegistrationNumberValid, consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(3)
        },
        f => fail("An error should occur")
      )
    }

    "reject if referenceNumber is less than min length" in {
      vehicleLookupFiller(referenceNumber = "1234567891", registrationNumber = vehicleRegistrationNumberValid, consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if referenceNumber is greater than max length" in {
      vehicleLookupFiller(referenceNumber = "123456789101", registrationNumber = vehicleRegistrationNumberValid, consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if referenceNumber contains letters" in {
      vehicleLookupFiller(referenceNumber = "qwertyuiopl", registrationNumber = vehicleRegistrationNumberValid, consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if referenceNumber contains special characters" in {
      vehicleLookupFiller(referenceNumber = "£££££££££££", registrationNumber = vehicleRegistrationNumberValid, consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if referenceNumber is valid" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = vehicleRegistrationNumberValid, consent = consentValid).fold(
        formWithErrors => {
          fail("An error should occur")
        },
        f => f.referenceNumber should equal(documentReferenceNumberValid)
      )
    }

    /**************************
     * registrationNumber tests
     */
    "reject if registrationNumber is empty" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(2)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is less than min length" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "a", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber is more than max length" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "AB53 WERT", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if registrationNumber contains special characters" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "ab53ab%", consent = consentValid).fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept if registrationNumber equals A 9" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "A 9", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("A 9")
      )
    }

    "accept if registrationNumber equals 9 A" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "9 A", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("9 A")
      )
    }

    "accept if registrationNumber equals AA 9" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "AA 9", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AA 9")
      )
    }

    "accept if registrationNumber equals A 99" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "A 99", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("A 99")
      )
    }

    "accept if registrationNumber equals 9 AA" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "9 AA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("9 AA")
      )
    }

    "accept if registrationNumber equals 99 A" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "99 A", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("99 A")
      )
    }

    "accept if registrationNumber equals AAA 9" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "AAA 9", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AAA 9")
      )
    }

    "accept if registrationNumber equals A 999" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "A 999", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("A 999")
      )
    }

    "accept if registrationNumber equals AA 99" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "AA 99", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AA 99")
      )
    }

    "accept if registrationNumber equals 9 AAA" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "9 AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("9 AAA")
      )
    }

    "accept if registrationNumber equals 99 AA" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "99 AA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("99 AA")
      )
    }

    "accept if registrationNumber equals 999 A" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "999 A", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("999 A")
      )
    }

    "accept if registrationNumber equals A9 AAA" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "A9 AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("A9 AAA")
      )
    }

    "accept if registrationNumber equals AAA 9A" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "AAA 9A", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AAA 9A")
      )
    }

    "accept if registrationNumber equals AAA 99" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "AAA 99", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AAA 99")
      )
    }

    "accept if registrationNumber equals AA 999" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "AA 999", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AA 999")
      )
    }

    "accept if registrationNumber equals 99 AAA" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "99 AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("99 AAA")
      )
    }

    "accept if registrationNumber equals 999 AA" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "999 AA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("999 AA")
      )
    }

    "accept if registrationNumber equals 9999 A" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "9999 A", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("9999 A")
      )
    }

    "accept if registrationNumber equals A 9999" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "A 9999", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("A 9999")
      )
    }

    "accept if registrationNumber equals A99 AAA" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "A99 AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("A99 AAA")
      )
    }

    "accept if registrationNumber equals AAA 99A" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "AAA 99A", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AAA 99A")
      )
    }

    "accept if registrationNumber equals AAA 999" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "AAA 999", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AAA 999")
      )
    }

    "accept if registrationNumber equals 999 AAA" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "999 AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("999 AAA")
      )
    }

    "accept if registrationNumber equals AA 9999" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "AA 9999", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AA 9999")
      )
    }

    "accept if registrationNumber equals 9999 AA" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "9999 AA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("9999 AA")
      )
    }

    "accept if registrationNumber equals A999 AAA" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "A999 AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("A999 AAA")
      )
    }

    "accept if registrationNumber equals AAA 999A" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "AAA 999A", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AAA 999A")
      )
    }

    "accept if registrationNumber equals AAA 9999" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "AAA 9999", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AAA 9999")
      )
    }

    "accept if registrationNumber equals AA99 AAA" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "AA99 AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("AA99 AAA")
      )
    }

    "accept if registrationNumber equals 9999 AAA" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = "9999 AAA", consent = consentValid).fold(
        formWithErrors => {fail("An error should occur")
        },
        f => f.registrationNumber should equal("9999 AAA")
      )
    }

    /***************
     * consent tests
     */
    "reject if consent is not ticked" in {
      vehicleLookupFiller(referenceNumber = documentReferenceNumberValid, registrationNumber = vehicleRegistrationNumberValid, consent = "").fold(
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

  }
}