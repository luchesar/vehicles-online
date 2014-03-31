package modules

import app.ConfigProperties._
import com.tzavellas.sse.guice.ScalaModule
import services._
import play.api.Logger
import services.fakes._
import services.address_lookup.{AddressLookupWebService, AddressLookupService}
import services.vehicle_lookup.{VehicleLookupWebServiceImpl, VehicleLookupWebService}

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
    bind[VehicleLookupWebService].to[VehicleLookupWebServiceImpl].asEagerSingleton() // TODO replace with new'ing up a fake web service.
    bind[VehicleLookupService].to[FakeVehicleLookupService].asEagerSingleton()
    bind[DisposeService].to[FakeDisposeService].asEagerSingleton()
  }

  private def ordnanceSurveyAddressLookup() = {
    bind[AddressLookupService].to[FakeAddressLookupService]
    val fakeWebServiceImpl = new FakeWebServiceImpl(
      responseOfPostcodeWebService = FakeWebServiceImpl.responseValidForOrdnanceSurvey,
      responseOfUprnWebService = FakeWebServiceImpl.responseValidForOrdnanceSurvey
    )
    bind[AddressLookupWebService].toInstance(fakeWebServiceImpl)
  }

  private def gdsAddressLookup() = {
    bind[AddressLookupService].to[FakeAddressLookupService]
    val fakeWebServiceImpl = new FakeWebServiceImpl(
      responseOfPostcodeWebService = FakeWebServiceImpl.responseValidForGdsAddressLookup,
      responseOfUprnWebService = FakeWebServiceImpl.responseValidForGdsAddressLookup
    )
    bind[AddressLookupWebService].toInstance(fakeWebServiceImpl)
  }
}