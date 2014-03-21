package modules

import com.tzavellas.sse.guice.ScalaModule
import play.api.Logger
import services._
import services.address_lookup.AddressLookupService

/**
 * Provides real implementations of traits
 * Note the use of sse-guice, which is a library that makes the Guice internal DSL more scala friendly
 * eg we can write this:
 * bind[Service].to[ServiceImpl].in[Singleton]
 * instead of this:
 * bind(classOf[Service]).to(classOf[ServiceImpl]).in(classOf[Singleton])
 *
 * Look in build.scala for where we import the sse-guice library
 */
object DevModule extends ScalaModule {
  def configure() {
    Logger.debug("Guice is loading DevModule")

    //ordnanceSurveyAddressLookup()
    gdsAddressLookup()
    bind[VehicleLookupService].to[VehicleLookupServiceImpl].asEagerSingleton
    bind[DisposeService].to[DisposeServiceImpl].asEagerSingleton
  }

  private def ordnanceSurveyAddressLookup() = {
    bind[AddressLookupService].to[services.address_lookup.ordnance_survey.AddressLookupServiceImpl].asEagerSingleton
    bind[WebService].to[services.address_lookup.ordnance_survey.WebServiceImpl].asEagerSingleton
  }

  private def gdsAddressLookup() = {
    bind[AddressLookupService].to[services.address_lookup.gds.AddressLookupServiceImpl].asEagerSingleton
    bind[WebService].to[services.address_lookup.gds.WebServiceImpl].asEagerSingleton
  }
}