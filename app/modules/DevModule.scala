package modules

import com.tzavellas.sse.guice.ScalaModule
import play.api.Logger
import services._
import modules.TestModule.FakeAddressLookupService

/**
 * Provides real implementations for traits
 */
object DevModule extends ScalaModule {
  def configure() {
    Logger.debug("Guice is loading DevModule")
    bind[V5cSearchWebService].to[V5cSearchWebServiceImpl]
    bind[LoginWebService].to[LoginWebServiceImpl]
    bind[AddressLookupService].to[FakeAddressLookupService]
  }
}