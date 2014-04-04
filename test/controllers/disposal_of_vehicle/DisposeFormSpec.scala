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
import services.{DateService, DateServiceImpl}

class DisposeFormSpec extends UnitSpec {

  "Dispose Form" should {
    def dateServiceStub(dayToday: Int = dateOfDisposalDayValid.toInt,
                        monthToday: Int = dateOfDisposalMonthValid.toInt,
                        yearToday: Int = dateOfDisposalYearValid.toInt) = {
      val dayMonthYearStub = new models.DayMonthYear(day = dayToday,
        month = monthToday,
        year = yearToday)
      val dateService = mock[DateServiceImpl]
      when(dateService.today).thenReturn(dayMonthYearStub)
      dateService
    }

    def dispose(dateService: DateService = dateServiceStub()) = {
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

      new disposal_of_vehicle.Dispose(disposeServiceImpl, dateService)
    }

    def formWithValidDefaults(mileage: String = mileageValid,
                              dayOfDispose: String = dateOfDisposalDayValid,
                              monthOfDispose: String = dateOfDisposalMonthValid,
                              yearOfDispose: String = dateOfDisposalYearValid,
                              disposeController: Dispose = dispose()) = {

      disposeController.disposeForm.bind(
        Map(
          mileageId -> mileage,
          s"$dateOfDisposalId.day" -> dayOfDispose,
          s"$dateOfDisposalId.month" -> monthOfDispose,
          s"$dateOfDisposalId.year" -> yearOfDispose
        )
      )
    }

    "reject if mileage is more than maximum" in {
      formWithValidDefaults(mileage = "1000000").errors should have length 1
    }

    "reject if date day is invalid" in {
      formWithValidDefaults(dayOfDispose = "").errors should have length 1
    }

    "reject if date month is invalid" in {
      formWithValidDefaults(monthOfDispose = "").errors should have length 1
    }

    "reject if date year is invalid" in {
      formWithValidDefaults(yearOfDispose = "").errors should have length 1
    }

    "accept when all fields contain valid responses" in {
      val model = formWithValidDefaults(
        mileage = mileageValid,
        dayOfDispose = dateOfDisposalDayValid,
        monthOfDispose = dateOfDisposalMonthValid,
        yearOfDispose = dateOfDisposalYearValid).get

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
        dayOfDispose = dateOfDisposalDayValid,
        monthOfDispose = dateOfDisposalMonthValid,
        yearOfDispose = dateOfDisposalYearValid).get

      model.mileage should equal(None)
      model.dateOfDisposal should equal(DayMonthYear(
        dateOfDisposalDayValid.toInt,
        dateOfDisposalMonthValid.toInt,
        dateOfDisposalYearValid.toInt))
    }

    "reject if date is in the future" in {
      // TODO write constraint and test
      pending
    }

    "reject if date is more than 2 years in the past" in {
      // TODO We need a fake DateService that we IoC in the TestModule.
      // TODO change this test so it is two years and one day in the past
      val yearToday: Int = 2014
      val dateServiceStubbedDateInFuture = dateServiceStub(yearToday = yearToday)
      val disposeController = dispose(dateService = dateServiceStubbedDateInFuture)
      formWithValidDefaults(yearOfDispose = (yearToday - 3).toString, disposeController = disposeController).errors should have length 1
    }
  }
}