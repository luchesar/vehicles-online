package modules

import com.tzavellas.sse.guice.ScalaModule
import services._
import play.api.Logger
import services.fakes._
import services.address_lookup.AddressLookupService

object TestModule extends ScalaModule {
  /**
   * Bind the fake implementations the traits
   */
  def configure() {
    Logger.debug("Guice is loading TestModule")

    bind[V5cSearchWebService].to[FakeV5cSearchWebService]
    bind[LoginWebService].to[FakeLoginWebService]
    bind[AddressLookupService].to[FakeAddressLookupService]
    bind[WebService].to[FakeWebServiceImpl].asEagerSingleton
    bind[VehicleLookupService].to[FakeVehicleLookupService].asEagerSingleton
    bind[DisposeService].to[FakeDisposeService].asEagerSingleton
  }
}