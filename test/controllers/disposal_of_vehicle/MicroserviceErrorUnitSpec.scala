package controllers.disposal_of_vehicle

import common.ClientSideSessionFactory
import helpers.UnitSpec
import org.mockito.Mockito.when
import play.api.test.Helpers._
import play.api.test.FakeRequest
import helpers.WithApplication
import utils.helpers.Config

final class MicroserviceErrorUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      status(present) should equal(OK)
    }

    "not display progress bar" in new WithApplication {
      contentAsString(present) should not include "Step "
    }

    "display prototype message when config set to true" in new WithApplication {
      contentAsString(present) should include("""<div class="prototype">""")
    }

    "not display prototype message when config set to false" in new WithApplication {
      val request = FakeRequest()
      implicit val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
      implicit val config: Config = mock[Config]
      when(config.isPrototypeBannerVisible).thenReturn(false) // Stub this config value.
      val microServiceErrorPrototypeNotVisible = new MicroServiceError()

      val result = microServiceErrorPrototypeNotVisible.present(request)
      contentAsString(result) should not include """<div class="prototype">"""
    }
  }

  private val microServiceError = injector.getInstance(classOf[MicroServiceError])

  private lazy val present = {
    microServiceError.present(FakeRequest())
  }
}
