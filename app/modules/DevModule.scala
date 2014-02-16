package modules

import com.tzavellas.sse.guice.ScalaModule
import play.api.Logger
import services._
import modules.TestModule.FakeAddressLookupService

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
    bind[V5cSearchWebService].to[V5cSearchWebServiceImpl].asEagerSingleton()
    bind[LoginWebService].to[LoginWebServiceImpl].asEagerSingleton()
    bind[AddressLookupService].to[FakeAddressLookupService].asEagerSingleton()
  }
}