package controllers.disposal_of_vehicle

import common.ClientSideSessionFactory
import controllers.disposal_of_vehicle
import helpers.UnitSpec
import mappings.common.DayMonthYear.{DayId, MonthId, YearId}
import mappings.common.Mileage
import mappings.disposal_of_vehicle.Dispose.{ConsentId, DateOfDisposalId, LossOfRegistrationConsentId, MileageId}
import models.DayMonthYear
import models.domain.disposal_of_vehicle.DisposeRequest
import org.mockito.Matchers.any
import org.mockito.Mockito.when
import play.api.libs.json.Json
import services.DateService
import services.dispose_service.{DisposeServiceImpl, DisposeWebService}
import services.fakes.FakeDateServiceImpl.{DateOfDisposalDayValid, DateOfDisposalMonthValid, DateOfDisposalYearValid}
import services.fakes.FakeDisposeWebServiceImpl.{ConsentValid, MileageValid, disposeResponseSuccess}
import services.fakes.FakeResponse
import utils.helpers.Config
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
 
final class DisposeFormSpec extends UnitSpec {
  "form" should {
    "accept when all fields contain valid responses" in {
      val model = formWithValidDefaults().get

      model.mileage.get should equal(MileageValid.toInt)
      model.dateOfDisposal should equal(DayMonthYear(
        DateOfDisposalDayValid.toInt,
        DateOfDisposalMonthValid.toInt,
        DateOfDisposalYearValid.toInt)
      )
      model.consent should equal(ConsentValid)
      model.lossOfRegistrationConsent should equal(ConsentValid)
    }

    "accept when all mandatory fields contain valid responses" in {
      val model = formWithValidDefaults(
        mileage = "",
        dayOfDispose = DateOfDisposalDayValid,
        monthOfDispose = DateOfDisposalMonthValid,
        yearOfDispose = DateOfDisposalYearValid).get

      model.mileage should equal(None)
      model.dateOfDisposal should equal(DayMonthYear(
        DateOfDisposalDayValid.toInt,
        DateOfDisposalMonthValid.toInt,
        DateOfDisposalYearValid.toInt))
    }
  }

  "mileage" should {
    "reject if mileage is more than maximum" in {
      formWithValidDefaults(mileage = (Mileage.Max + 1).toString).errors should have length 1
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
      val dayToday: Int = DateOfDisposalDayValid.toInt
      val dayOfDispose = (dayToday + 1).toString

      // Attempting to dispose with a date 1 day into the future.
      val result = formWithValidDefaults(
        dayOfDispose = dayOfDispose)

      result.errors should have length 1
      result.errors(0).key should equal(DateOfDisposalId)
      result.errors(0).message should equal("error.notInFuture")
    }

    "reject if date is more than 2 years in the past" in {
      val dayToday: Int = DateOfDisposalDayValid.toInt
      val yearToday: Int = DateOfDisposalYearValid.toInt
      val dayOfDispose = (dayToday - 1).toString
      val yearOfDispose = (yearToday - 2).toString

      // Attempting to dispose with a date 2 years and 1 day into the past.
      val result = formWithValidDefaults(
        dayOfDispose = dayOfDispose,
        yearOfDispose = yearOfDispose)

      result.errors should have length 1
      result.errors(0).key should equal(DateOfDisposalId)
      result.errors(0).message should equal("error.withinTwoYears")
    }

    "reject if date is too far in the past" in {
      val yearOfDispose = "1"
      val dateServiceStubbed = dateServiceStub(yearToday = 1)

      // Attempting to dispose with a date 2 years and 1 day into the past.
      val result = formWithValidDefaults(yearOfDispose = yearOfDispose,
        disposeController = dispose(dateServiceStubbed))

      result.errors should have length 1
      result.errors(0).key should equal(DateOfDisposalId)
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

  private def dateServiceStub(dayToday: Int = DateOfDisposalDayValid.toInt,
                              monthToday: Int = DateOfDisposalMonthValid.toInt,
                              yearToday: Int = DateOfDisposalYearValid.toInt) = {
    val dayMonthYearStub = new models.DayMonthYear(day = dayToday,
      month = monthToday,
      year = yearToday)
    val dateService = mock[DateService]
    when(dateService.today).thenReturn(dayMonthYearStub)
    dateService
  }

  private def dispose(dateService: DateService = dateServiceStub()) = {
    val ws = mock[DisposeWebService]
    when(ws.callDisposeService(any[DisposeRequest], any[String])).thenReturn(Future {
      val responseAsJson = Json.toJson(disposeResponseSuccess)
      import play.api.http.Status.OK
      new FakeResponse(status = OK, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
    })
    val disposeServiceImpl = new DisposeServiceImpl(new Config(), ws)
    implicit val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
    implicit val config: Config = mock[Config]
    new disposal_of_vehicle.Dispose(disposeServiceImpl, dateService)
  }

  private def formWithValidDefaults(mileage: String = MileageValid,
                                    dayOfDispose: String = DateOfDisposalDayValid,
                                    monthOfDispose: String = DateOfDisposalMonthValid,
                                    yearOfDispose: String = DateOfDisposalYearValid,
                                    consent: String = ConsentValid,
                                    lossOfRegistrationConsent: String = ConsentValid,
                                    disposeController: Dispose = dispose()) = {

    disposeController.form.bind(
      Map(
        MileageId -> mileage,
        s"$DateOfDisposalId.$DayId" -> dayOfDispose,
        s"$DateOfDisposalId.$MonthId" -> monthOfDispose,
        s"$DateOfDisposalId.$YearId" -> yearOfDispose,
        ConsentId -> consent,
        LossOfRegistrationConsentId -> lossOfRegistrationConsent
      )
    )
  }
}