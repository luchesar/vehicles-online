package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.Dispose._
import helpers.disposal_of_vehicle.Helper._
import models.domain.disposal_of_vehicle.DisposeModel
import org.mockito.Mockito._
import models.domain.disposal_of_vehicle.DisposeModel
import org.mockito.Matchers._
import models.domain.disposal_of_vehicle.DisposeModel
import services.fakes.FakeDisposeSuccessService
import controllers.disposal_of_vehicle
import org.scalatest.mock.MockitoSugar

class DisposeFormSpec extends WordSpec with Matchers with MockitoSugar {
  "Dispose Form" should {
    val mockDisposeModel = mock[DisposeModel]
    val mockWebService = mock[services.DisposeService]
    when(mockWebService.invoke(any[DisposeModel])).thenReturn(new FakeDisposeSuccessService().invoke(mockDisposeModel))
    val dispose = new disposal_of_vehicle.Dispose(mockWebService)

    def disposeFormFiller(consent: String, mileage: String, day: String, month: String, year: String) = {
      dispose.disposeForm.bind(
        Map(
          consentId -> consent,
          mileageId -> mileage,
          s"${dateOfDisposalId}.day" -> day,
          s"${dateOfDisposalId}.month" -> month,
          s"${dateOfDisposalId}.year" -> year
        )
      )
    }

    "reject if mileage is more than maximum" in {
      disposeFormFiller(consent = consentValid, mileage = "1000000", day = dateOfDisposalDayValid, month = dateOfDisposalMonthValid, year = dateOfDisposalYearValid).fold (
        formWithErrors => {
          println(formWithErrors.errors)
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if consent is not ticked" in {
      disposeFormFiller(consent = "", mileage = mileageValid, day = dateOfDisposalDayValid, month = dateOfDisposalMonthValid, year = dateOfDisposalYearValid).fold (
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if date day is invalid" in {
      disposeFormFiller(consent = consentValid, mileage = mileageValid, day = "", month = dateOfDisposalMonthValid, year = dateOfDisposalYearValid).fold (
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if date month is invalid" in {
      disposeFormFiller(consent = consentValid, mileage = mileageValid, day = dateOfDisposalDayValid, month = "", year = dateOfDisposalYearValid).fold (
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if date year is invalid" in {
      disposeFormFiller(consent = consentValid, mileage = mileageValid, day = dateOfDisposalDayValid, month = dateOfDisposalMonthValid, year = "").fold (
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept when all fields contain valid responses" in {
      disposeFormFiller(consent = consentValid, mileage = mileageValid, day = dateOfDisposalDayValid, month = dateOfDisposalMonthValid, year = dateOfDisposalYearValid).fold(
      formWithErrors => {
          fail("An error should occur")
        },
        f => f.consent should equal(consentValid)
      )
    }
  }
}