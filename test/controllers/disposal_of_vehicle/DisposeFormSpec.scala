package controllers.disposal_of_vehicle

import org.scalatest.{Matchers, WordSpec}
import mappings.disposal_of_vehicle.Dispose._
import helpers.disposal_of_vehicle.Helper._
import models.domain.disposal_of_vehicle.DisposeModel
import org.mockito.Mockito._
import models.domain.disposal_of_vehicle.DisposeModel
import org.mockito.Matchers._
import models.domain.disposal_of_vehicle.DisposeModel
import services.fakes.FakeDisposeService
import controllers.disposal_of_vehicle
import org.scalatest.mock.MockitoSugar
import models.DayMonthYear

class DisposeFormSpec extends WordSpec with Matchers with MockitoSugar {
  "Dispose Form" should {
    val mockDisposeModel = mock[DisposeModel]
    val mockWebService = mock[services.DisposeService]
    when(mockWebService.invoke(any[DisposeModel])).thenReturn(new FakeDisposeService().invoke(mockDisposeModel))
    val dispose = new disposal_of_vehicle.Dispose(mockWebService)

    def disposeFormFiller(mileage: String, day: String, month: String, year: String) = {
      dispose.disposeForm.bind(
        Map(
          mileageId -> mileage,
          s"${dateOfDisposalId}.day" -> day,
          s"${dateOfDisposalId}.month" -> month,
          s"${dateOfDisposalId}.year" -> year
        )
      )
    }

    "reject if mileage is more than maximum" in {
      disposeFormFiller(mileage = "1000000", day = dateOfDisposalDayValid, month = dateOfDisposalMonthValid, year = dateOfDisposalYearValid).fold (
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if date day is invalid" in {
      disposeFormFiller(mileage = mileageValid, day = "", month = dateOfDisposalMonthValid, year = dateOfDisposalYearValid).fold (
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if date month is invalid" in {
      disposeFormFiller(mileage = mileageValid, day = dateOfDisposalDayValid, month = "", year = dateOfDisposalYearValid).fold (
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "reject if date year is invalid" in {
      disposeFormFiller(mileage = mileageValid, day = dateOfDisposalDayValid, month = dateOfDisposalMonthValid, year = "").fold (
        formWithErrors => {
          formWithErrors.errors.length should equal(1)
        },
        f => fail("An error should occur")
      )
    }

    "accept when all fields contain valid responses" in {
      disposeFormFiller(mileage = mileageValid, day = dateOfDisposalDayValid, month = dateOfDisposalMonthValid, year = dateOfDisposalYearValid).fold(
      formWithErrors => {
          fail("An error should occur")
        },
        f => {
          f.mileage.get should equal(mileageValid.toInt)
          f.dateOfDisposal should equal(DayMonthYear(Some(dateOfDisposalDayValid.toInt), Some(dateOfDisposalMonthValid.toInt), Some(dateOfDisposalYearValid.toInt)))
        }
      )
    }

    "accept when all mandatory fields contain valid responses" in {
      disposeFormFiller(mileage = "", day = dateOfDisposalDayValid, month = dateOfDisposalMonthValid, year = dateOfDisposalYearValid).fold(
        formWithErrors => {
          fail("An error should occur")
        },
        f => {
          f.mileage should equal(None)
          f.dateOfDisposal should equal(DayMonthYear(Some(dateOfDisposalDayValid.toInt), Some(dateOfDisposalMonthValid.toInt), Some(dateOfDisposalYearValid.toInt)))
        }
      )
    }
  }
}