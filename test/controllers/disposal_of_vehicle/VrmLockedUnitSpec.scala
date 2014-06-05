package controllers.disposal_of_vehicle

import helpers.{WithApplication, UnitSpec}
import composition.TestComposition.{testInjector => injector}
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import services.fakes.FakeDateServiceImpl
import play.api.test.FakeRequest

final class VrmLockedUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      val dateService = new FakeDateServiceImpl
      val request = FakeRequest().
        withCookies(CookieFactoryForUnitSpecs.trackingIdModel("x" * 20)). // TODO replace "x" * 20 with a constant in CookieFactoryForUnitSpecs
        withCookies(CookieFactoryForUnitSpecs.bruteForcePreventionViewModel(dateTimeISOChronology = dateService.dateTimeISOChronology))
      val vrmLocked = injector.getInstance(classOf[VrmLocked])

      val result = vrmLocked.present(request)

      whenReady(result) {
        r => r.header.status should equal(play.api.http.Status.OK)
      }
    }
  }
}