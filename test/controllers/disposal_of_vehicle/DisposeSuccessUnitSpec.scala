package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import pages.disposal_of_vehicle._
import helpers.disposal_of_vehicle.CacheSetup
import helpers.UnitSpec

class DisposeSuccessUnitSpec extends UnitSpec {
  private def cacheSetup() = {
    CacheSetup.setupTradeDetails().
      businessChooseYourAddress().
      vehicleDetailsModel().
      disposeFormModel().
      disposeTransactionId().
      vehicleRegistrationNumber()
  }

  "Disposal success controller" should {
    "present" in new WithApplication {
      cacheSetup()
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.present(request)
      whenReady(result) {
        r => r.header.status should equal(OK)
      }
    }

    "redirect to next page after the new disposal button is clicked" in new WithApplication {
      cacheSetup()
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(VehicleLookupPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only DealerDetails are cached" in new WithApplication {
      CacheSetup.businessChooseYourAddress()
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails are cached" in new WithApplication {
      CacheSetup.vehicleDetailsModel()
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only DisposeDetails are cached" in new WithApplication {
      CacheSetup.disposeModel()
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails and DisposeDetails are cached" in new WithApplication {
      CacheSetup.vehicleDetailsModel().
        disposeFormModel()
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails and DealerDetails are cached" in new WithApplication {
      CacheSetup.vehicleDetailsModel().
        businessChooseYourAddress()
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on present when only DisposeDetails and DealerDetails are cached" in new WithApplication {
      CacheSetup.businessChooseYourAddress().
        disposeModel()
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.present(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only DealerDetails are cached" in new WithApplication {
      CacheSetup.businessChooseYourAddress()
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails are cached" in new WithApplication {
      CacheSetup.vehicleDetailsModel()
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only DisposeDetails are cached" in new WithApplication {
      CacheSetup.disposeModel()
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails and DisposeDetails are cached" in new WithApplication {
      CacheSetup.vehicleDetailsModel().
        disposeFormModel()
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails and DealerDetails are cached" in new WithApplication {
      CacheSetup.vehicleDetailsModel().
       businessChooseYourAddress()
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }

    "redirect to SetUpTradeDetails on submit when only DisposeDetails and DealerDetails are cached" in new WithApplication {
      CacheSetup.businessChooseYourAddress().
        disposeModel()
      val request = FakeRequest().withSession()
      val result = disposal_of_vehicle.DisposeSuccess.submit(request)
      whenReady(result) {
        r => r.header.headers.get(LOCATION) should equal(Some(SetupTradeDetailsPage.address))
      }
    }
  }
}