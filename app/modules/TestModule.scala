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

    //ordnanceSurveyAddressLookup()
    gdsAddressLookup()
    bind[VehicleLookupService].to[FakeVehicleLookupService].asEagerSingleton()
    bind[DisposeService].to[FakeDisposeService].asEagerSingleton()
  }

  private def ordnanceSurveyAddressLookup() = {
    bind[AddressLookupService].to[FakeAddressLookupService]
    val fakeWebServiceImpl = new FakeWebServiceImpl(
      responseOfPostcodeWebService = FakeWebServiceImpl.responseValidForOrdnanceSurvey,
      responseOfUprnWebService = FakeWebServiceImpl.responseValidForOrdnanceSurvey
    )
    bind[WebService].toInstance(fakeWebServiceImpl)
  }

  private def gdsAddressLookup() = {
    bind[AddressLookupService].to[FakeAddressLookupService]
    val fakeWebServiceImpl = new FakeWebServiceImpl(
      responseOfPostcodeWebService = FakeWebServiceImpl.responseValidForGdsAddressLookup,
      responseOfUprnWebService = FakeWebServiceImpl.responseValidForGdsAddressLookup
    )
    bind[WebService].toInstance(fakeWebServiceImpl)
  }
}