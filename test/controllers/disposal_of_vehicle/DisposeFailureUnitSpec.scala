package controllers.disposal_of_vehicle

import common.ClientSideSessionFactory
import helpers.common.CookieHelper
import org.mockito.Mockito._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import helpers.disposal_of_vehicle.CookieFactoryForUnitSpecs
import helpers.UnitSpec
import helpers.WithApplication
import pages.disposal_of_vehicle.DisposeFailurePage
import CookieHelper._
import utils.helpers.Config
import scala.Some
import play.api.Play

final class DisposeFailureUnitSpec extends UnitSpec {
  "present" should {
    "display the page" in new WithApplication {
      whenReady(present) {
        r => r.header.status should equal(OK)
      }
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
      when(config.isPrototypeBannerVisible).thenReturn(false)
      val disposeFailurePrototypeNotVisible = new DisposeFailure()

      val result = disposeFailurePrototypeNotVisible.present(request)
      contentAsString(result) should not include """<div class="prototype">"""
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

