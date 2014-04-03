package controllers.disposal_of_vehicle

import mappings.disposal_of_vehicle.Dispose._
import helpers.disposal_of_vehicle.Helper._
import org.mockito.Mockito._
import org.mockito.Matchers._
import models.domain.disposal_of_vehicle.{DisposeResponse, DisposeRequest}
import controllers.disposal_of_vehicle
import models.DayMonthYear
import helpers.UnitSpec
import services.dispose_service.{DisposeWebService, DisposeServiceImpl}
import services.fakes.FakeResponse
import scala.concurrent.{ExecutionContext, Future}
import play.api.libs.json.Json
import ExecutionContext.Implicits.global

class DisposeFormSpec extends UnitSpec {

  "Dispose Form" should {
    val dispose = {
      val ws = mock[DisposeWebService]
      when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
        val disposeResponse =
          DisposeResponse(success = true,
            message = "Fake Web Dispose Service - Good response",
            transactionId = "1234",
            registrationNumber = registrationNumberValid,
            auditId = "7575")
        val responseAsJson = Json.toJson(disposeResponse)

        new FakeResponse(status = 200, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
      })

      val disposeServiceImpl = new DisposeServiceImpl(ws)

      new disposal_of_vehicle.Dispose(disposeServiceImpl)
    }

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
        dateOfDisposalDayValid.toInt,
        dateOfDisposalMonthValid.toInt,
        dateOfDisposalYearValid.toInt)
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
        dateOfDisposalDayValid.toInt,
        dateOfDisposalMonthValid.toInt,
        dateOfDisposalYearValid.toInt))
    }
  }
}