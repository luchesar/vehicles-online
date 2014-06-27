package controllers.disposal_of_vehicle

import helpers.{UnitSpec, WithApplication}
import play.api.test.FakeRequest
import play.api.test.Helpers._

final class HelpUnitSpec extends UnitSpec {
  "present" should {
    "display the error page" in new WithApplication {
      val result = help.present(FakeRequest())
      status(result) should equal(OK)
    }
  }

  private val help = injector.getInstance(classOf[Help])
}
