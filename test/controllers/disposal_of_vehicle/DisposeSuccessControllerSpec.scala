package controllers.disposal_of_vehicle

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import controllers.disposal_of_vehicle
import helpers.disposal_of_vehicle.{VehicleLookupPage, DisposePage, BusinessChooseYourAddressPage, SetUpTradeDetailsPage, DisposeSuccessPage}
import helpers.UnitSpec

class DisposeSuccessUnitSpec extends UnitSpec {

  "Disposal confirmation controller" should {

    "present" in new WithApplication {
      DisposeSuccessPage.happyPath()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      status(result) should equal(OK)
    }

    "redirect to next page after the new disposal button is clicked" in new WithApplication {
      DisposeSuccessPage.happyPath()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      redirectLocation(result) should equal(Some(VehicleLookupPage.url))
    }

    "redirect to SetUpTradeDetails on present when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to SetUpTradeDetails on present when only DealerDetails are cached" in new WithApplication {
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails are cached" in new WithApplication {
      VehicleLookupPage.setupVehicleDetailsModelCache()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to SetUpTradeDetails on present when only DisposeDetails are cached" in new WithApplication {
      DisposePage.setupDisposeFormModelCache()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails and DisposeDetails are cached" in new WithApplication {
      VehicleLookupPage.setupVehicleDetailsModelCache()
      DisposePage.setupDisposeFormModelCache()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to SetUpTradeDetails on present when only VehicleDetails and DealerDetails are cached" in new WithApplication {
      VehicleLookupPage.setupVehicleDetailsModelCache()
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to SetUpTradeDetails on present when only DisposeDetails and DealerDetails are cached" in new WithApplication {
      BusinessChooseYourAddressPage.setupCache()
      DisposePage.setupDisposeFormModelCache()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.present(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to SetUpTradeDetails on submit when cache is empty" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to SetUpTradeDetails on submit when only DealerDetails are cached" in new WithApplication {
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails are cached" in new WithApplication {
      VehicleLookupPage.setupVehicleDetailsModelCache()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to SetUpTradeDetails on submit when only DisposeDetails are cached" in new WithApplication {
      DisposePage.setupDisposeFormModelCache()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails and DisposeDetails are cached" in new WithApplication {
      VehicleLookupPage.setupVehicleDetailsModelCache()
      DisposePage.setupDisposeFormModelCache()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to SetUpTradeDetails on submit when only VehicleDetails and DealerDetails are cached" in new WithApplication {
      VehicleLookupPage.setupVehicleDetailsModelCache()
      BusinessChooseYourAddressPage.setupCache()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }

    "redirect to SetUpTradeDetails on submit when only DisposeDetails and DealerDetails are cached" in new WithApplication {
      BusinessChooseYourAddressPage.setupCache()
      DisposePage.setupDisposeFormModelCache()
      val request = FakeRequest().withSession()

      val result = disposal_of_vehicle.DisposeSuccess.submit(request)

      redirectLocation(result) should equal(Some(SetUpTradeDetailsPage.url))
    }
  }
}