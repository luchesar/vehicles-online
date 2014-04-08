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
import services.fakes.FakeDateServiceImpl._
import services.fakes.FakeVehicleLookupWebService._

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
            transactionId = "1234", // TODO don't use magic number, use a constant!
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
                              consent: String = consentValid,
                              disposeController: Dispose = dispose()) = {

      disposeController.disposeForm.bind(
        Map(
          mileageId -> mileage,
          s"$dateOfDisposalId.day" -> dayOfDispose,
          s"$dateOfDisposalId.month" -> monthOfDispose,
          s"$dateOfDisposalId.year" -> yearOfDispose,
          consentId -> consent
        )
      )
    }

    "reject if mileage is more than maximum" in {
      formWithValidDefaults(mileage = "1000000").errors should have length 1
    }

    "reject if date day is not selected" in {
      formWithValidDefaults(dayOfDispose = "").errors should have length 1
    }

    "reject if date month is not selected" in {
      formWithValidDefaults(monthOfDispose = "").errors should have length 1
    }

    "reject if date year is not selected" in {
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
      val dayToday: Int = dateOfDisposalDayValid.toInt
      val dateServiceStubbed = dateServiceStub()
      val disposeController = dispose(dateService = dateServiceStubbed)
      val dayOfDispose = (dayToday + 1).toString

      // Attempting to dispose with a date 1 day into the future.
      val result = formWithValidDefaults(
        dayOfDispose = dayOfDispose,
        disposeController = disposeController)

      result.errors should have length 1
      result.errors(0).key should equal("dateOfDisposal")
      result.errors(0).message should equal("error.notInFuture")
    }

    "reject if date is more than 2 years in the past" in {
      val dayToday: Int = dateOfDisposalDayValid.toInt
      val yearToday: Int = dateOfDisposalYearValid.toInt
      val dateServiceStubbed = dateServiceStub()
      val disposeController = dispose(dateService = dateServiceStubbed)
      val dayOfDispose = (dayToday - 1).toString
      val yearOfDispose = (yearToday - 2).toString

      // Attempting to dispose with a date 2 years and 1 day into the past.
      val result = formWithValidDefaults(
        dayOfDispose = dayOfDispose,
        yearOfDispose = yearOfDispose,
        disposeController = disposeController)

      result.errors should have length 1
      result.errors(0).key should equal("dateOfDisposal")
      result.errors(0).message should equal("error.withinTwoYears")
    }

    "reject if consent is not ticked" in {
      formWithValidDefaults(consent = "").errors should have length 1
    }
  }

}