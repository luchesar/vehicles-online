package controllers.disposal_of_vehicle

import mappings.disposal_of_vehicle.Dispose._
import helpers.disposal_of_vehicle.Helper._
import org.mockito.Mockito._
import org.mockito.Matchers._
import models.domain.disposal_of_vehicle.DisposeModel
import services.fakes.FakeDisposeService
import controllers.disposal_of_vehicle
import models.DayMonthYear
import helpers.UnitSpec

class DisposeFormSpec extends UnitSpec {

  "Dispose Form" should {
    val mockDisposeModel = mock[DisposeModel]
    val mockWebService = mock[services.DisposeService]
    when(mockWebService.invoke(any[DisposeModel])).thenReturn(new FakeDisposeService().invoke(mockDisposeModel))
    val dispose = new disposal_of_vehicle.Dispose(mockWebService)

    def formWithValidDefaults(mileage: String = mileageValid,
                   day: String = dateOfDisposalDayValid,
                   month: String = dateOfDisposalMonthValid,
                   year: String = dateOfDisposalYearValid) = {

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
      formWithValidDefaults(mileage = "1000000").errors should have length 1
    }

    "reject if date day is invalid" in {
      formWithValidDefaults(day = "").errors should have length 1
    }

    "reject if date month is invalid" in {
      formWithValidDefaults(month = "").errors should have length 1
    }

    "reject if date year is invalid" in {
      formWithValidDefaults(year = "").errors should have length 1
    }

    "accept when all fields contain valid responses" in {
      val model = formWithValidDefaults(
        mileage = mileageValid,
        day = dateOfDisposalDayValid,
        month = dateOfDisposalMonthValid,
        year = dateOfDisposalYearValid).get

      model.mileage.get should equal(mileageValid.toInt)
      model.dateOfDisposal should equal(DayMonthYear(
        Some(dateOfDisposalDayValid.toInt),
        Some(dateOfDisposalMonthValid.toInt),
        Some(dateOfDisposalYearValid.toInt))
      )
    }

    "accept when all mandatory fields contain valid responses" in {
      val model = formWithValidDefaults(
        mileage = "",
        day = dateOfDisposalDayValid,
        month = dateOfDisposalMonthValid,
        year = dateOfDisposalYearValid).get

      model.mileage should equal(None)
      model.dateOfDisposal should equal(DayMonthYear(
        Some(dateOfDisposalDayValid.toInt),
        Some(dateOfDisposalMonthValid.toInt),
        Some(dateOfDisposalYearValid.toInt)))
    }
  }
}