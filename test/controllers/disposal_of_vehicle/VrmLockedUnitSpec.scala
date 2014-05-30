package controllers.disposal_of_vehicle

import helpers.{WithApplication, UnitSpec}
import play.api.test.FakeRequest
import services.fakes.FakeDateServiceImpl._
import services.DateService
import org.mockito.Mockito._
import composition.TestComposition.{testInjector => injector}
import common.ClientSideSessionFactory
import play.api.http.Status._

final class VrmLockedUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val request = FakeRequest().withSession()
      val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
      val result = new controllers.disposal_of_vehicle.VrmLocked(dateServiceStubbed())(clientSideSessionFactory).present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }
  }

  private def dateServiceStubbed(day: Int = dateOfDisposalDayValid.toInt,
                                 month: Int = dateOfDisposalMonthValid.toInt,
                                 year: Int = dateOfDisposalYearValid.toInt) = {
    val dateService = mock[DateService]
    when(dateService.today).thenReturn(new models.DayMonthYear(day = day,
      month = month,
      year = year))
    dateService
  }

}