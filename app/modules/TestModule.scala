package modules

import app.ConfigProperties._
import com.tzavellas.sse.guice.ScalaModule
import services._
import play.api.Logger
import services.fakes._
import services.address_lookup.{AddressLookupWebService, AddressLookupService}
import services.vehicle_lookup.{VehicleLookupServiceImpl, VehicleLookupService, VehicleLookupWebServiceImpl, VehicleLookupWebService}
import services.dispose_service.{DisposeServiceImpl, DisposeWebServiceImpl, DisposeWebService, DisposeService}

object TestModule extends ScalaModule {
  /**
   * Bind the fake implementations the traits
   */
  def configure() {
    Logger.debug("Guice is loading TestModule")

    getProperty("addressLookupService.type", "ordnanceSurvey") match {
      case "ordnanceSurvey" => ordnanceSurveyAddressLookup()
      case _ => gdsAddressLookup()
    }
    bind[VehicleLookupWebService].to[FakeVehicleLookupWebService].asEagerSingleton()
    bind[VehicleLookupService].to[VehicleLookupServiceImpl].asEagerSingleton()
    bind[DisposeWebService].to[FakeDisposeWebServiceImpl].asEagerSingleton()
    bind[DisposeService].to[DisposeServiceImpl].asEagerSingleton()
  }

  private def ordnanceSurveyAddressLookup() = {
    bind[AddressLookupService].to[services.address_lookup.ordnance_survey.AddressLookupServiceImpl]
    val fakeWebServiceImpl = new FakeWebServiceImpl(
      responseOfPostcodeWebService = FakeWebServiceImpl.responseValidForOrdnanceSurvey,
      responseOfUprnWebService = FakeWebServiceImpl.responseValidForOrdnanceSurvey
    )
    bind[AddressLookupWebService].toInstance(fakeWebServiceImpl)
  }

  private def gdsAddressLookup() = {
    bind[AddressLookupService].to[services.address_lookup.gds.AddressLookupServiceImpl]
    val fakeWebServiceImpl = new FakeWebServiceImpl(
      responseOfPostcodeWebService = FakeWebServiceImpl.responseValidForGdsAddressLookup,
      responseOfUprnWebService = FakeWebServiceImpl.responseValidForGdsAddressLookup
    )
    bind[AddressLookupWebService].toInstance(fakeWebServiceImpl)
  }
}