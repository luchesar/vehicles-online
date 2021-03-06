package controllers.disposal_of_vehicle

import common.ClientSideSessionFactory
import controllers.disposal_of_vehicle.Common.PrototypeHtml
import helpers.{UnitSpec, WithApplication}
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import org.mockito.Mockito.when
import play.api.test.FakeRequest
import play.api.test.Helpers.{OK, contentAsString, defaultAwaitTimeout}
import utils.helpers.Config

final class DisposeFailureUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      whenReady(present) { r =>
        r.header.status should equal(OK)
      }
    }

    "not display progress bar" in new WithApplication {
      contentAsString(present) should not include "Step "
    }

    "display prototype message when config set to true" in new WithApplication {
      contentAsString(present) should include(PrototypeHtml)
    }

    "not display prototype message when config set to false" in new WithApplication {
      val request = FakeRequest()
      implicit val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
      implicit val config: Config = mock[Config]
      when(config.isPrototypeBannerVisible).thenReturn(false) // Stub this config value.
      val disposeFailurePrototypeNotVisible = new DisposeFailure()

      val result = disposeFailurePrototypeNotVisible.present(request)
      contentAsString(result) should not include PrototypeHtml
    }
  }

  private lazy val present = {
    val disposeFailure = injector.getInstance(classOf[DisposeFailure])
    val request = FakeRequest().
      withCookies(CookieFactoryForUnitSpecs.traderDetailsModel()).
      withCookies(CookieFactoryForUnitSpecs.vehicleDetailsModel()).
      withCookies(CookieFactoryForUnitSpecs.disposeFormModel()).
      withCookies(CookieFactoryForUnitSpecs.disposeTransactionId())
    disposeFailure.present(request)
  }
}