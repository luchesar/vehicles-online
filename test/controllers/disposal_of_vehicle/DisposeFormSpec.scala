package controllers.disposal_of_vehicle

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import controllers.disposal_of_vehicle
import helpers.UnitSpec
import mappings.common.DayMonthYear._
import mappings.common.Mileage
import mappings.disposal_of_vehicle.Dispose._
import models.DayMonthYear
import models.domain.disposal_of_vehicle.DisposeRequest
import org.mockito.Matchers._
import org.mockito.Mockito._
import play.api.libs.json.Json
import services.dispose_service.{DisposeWebService, DisposeServiceImpl}
import services.fakes.FakeDateServiceImpl._
import services.fakes.FakeDisposeWebServiceImpl._
import services.fakes.FakeResponse
import services.{DateService, DateServiceImpl}

class DisposeFormSpec extends UnitSpec {

  "form" should {
    "accept when all fields contain valid responses" in {
      val model = formWithValidDefaults().get

      model.mileage.get should equal(mileageValid.toInt)
      model.dateOfDisposal should equal(DayMonthYear(
        dateOfDisposalDayValid.toInt,
        dateOfDisposalMonthValid.toInt,
        dateOfDisposalYearValid.toInt)
      )
      model.consent should equal(consentValid)
      model.lossOfRegistrationConsent should equal(consentValid)
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
  }

  "mileage" should {
    "reject if mileage is more than maximum" in {
      formWithValidDefaults(mileage = (Mileage.max + 1).toString).errors should have length 1
    }
  }

  "dateOfDisposal" should {
    "reject if date day is not selected" in {
      formWithValidDefaults(dayOfDispose = "").errors should have length 1
    }

    "reject if date month is not selected" in {
      formWithValidDefaults(monthOfDispose = "").errors should have length 1
    }

    "reject if date year is not selected" in {
      formWithValidDefaults(yearOfDispose = "").errors should have length 1
    }

    "reject if date is in the future" in {
      val dayToday: Int = dateOfDisposalDayValid.toInt
      val dayOfDispose = (dayToday + 1).toString

      // Attempting to dispose with a date 1 day into the future.
      val result = formWithValidDefaults(
        dayOfDispose = dayOfDispose)

      result.errors should have length 1
      result.errors(0).key should equal(dateOfDisposalId)
      result.errors(0).message should equal("error.notInFuture")
    }

    "reject if date is more than 2 years in the past" in {
      val dayToday: Int = dateOfDisposalDayValid.toInt
      val yearToday: Int = dateOfDisposalYearValid.toInt
      val dayOfDispose = (dayToday - 1).toString
      val yearOfDispose = (yearToday - 2).toString

      // Attempting to dispose with a date 2 years and 1 day into the past.
      val result = formWithValidDefaults(
        dayOfDispose = dayOfDispose,
        yearOfDispose = yearOfDispose)

      result.errors should have length 1
      result.errors(0).key should equal(dateOfDisposalId)
      result.errors(0).message should equal("error.withinTwoYears")
    }

    "reject if date is too far in the past" in {
      val yearOfDispose = "1"
      val dateServiceStubbed = dateServiceStub(yearToday = 1)

      // Attempting to dispose with a date 2 years and 1 day into the past.
      val result = formWithValidDefaults(yearOfDispose = yearOfDispose,
        disposeController = dispose(dateServiceStubbed))

      result.errors should have length 1
      result.errors(0).key should equal(dateOfDisposalId)
      result.errors(0).message should equal("error.invalid")
    }
  }

  "consent" should {
    "reject if consent is not ticked" in {
      formWithValidDefaults(consent = "").errors should have length 1
    }
  }

  "lossOfRegistrationConsent" should {
    "reject if loss of registration consent is not ticked" in {
      formWithValidDefaults(lossOfRegistrationConsent = "").errors should have length 1
    }
  }

  private def dateServiceStub(dayToday: Int = dateOfDisposalDayValid.toInt,
                              monthToday: Int = dateOfDisposalMonthValid.toInt,
                              yearToday: Int = dateOfDisposalYearValid.toInt) = {
    val dayMonthYearStub = new models.DayMonthYear(day = dayToday,
      month = monthToday,
      year = yearToday)
    val dateService = mock[DateServiceImpl]
    when(dateService.today).thenReturn(dayMonthYearStub)
    dateService
  }

  private def dispose(dateService: DateService = dateServiceStub()) = {
    val ws = mock[DisposeWebService]
    when(ws.callDisposeService(any[DisposeRequest])).thenReturn(Future {
      val responseAsJson = Json.toJson(disposeResponseSuccess)
      import play.api.http.Status.OK
      new FakeResponse(status = OK, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
    })
    val disposeServiceImpl = new DisposeServiceImpl(ws)
    new disposal_of_vehicle.Dispose( disposeServiceImpl, dateService)
  }

  private def formWithValidDefaults(mileage: String = mileageValid,
                                    dayOfDispose: String = dateOfDisposalDayValid,
                                    monthOfDispose: String = dateOfDisposalMonthValid,
                                    yearOfDispose: String = dateOfDisposalYearValid,
                                    consent: String = consentValid,
                                    lossOfRegistrationConsent: String = consentValid,
                                    disposeController: Dispose = dispose()) = {

    disposeController.disposeForm.bind(
      Map(
        mileageId -> mileage,
        s"$dateOfDisposalId.$dayId" -> dayOfDispose,
        s"$dateOfDisposalId.$monthId" -> monthOfDispose,
        s"$dateOfDisposalId.$yearId" -> yearOfDispose,
        consentId -> consent,
        lossOfRegistrationConsentId -> lossOfRegistrationConsent
      )
    )
  }

}
