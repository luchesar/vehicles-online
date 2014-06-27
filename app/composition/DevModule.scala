package composition

import app.ConfigProperties._
import com.google.inject.name.Names
import com.tzavellas.sse.guice.ScalaModule
import common._
import filters.AccessLoggingFilter._
import play.api.{LoggerLike, Logger}
import services.address_lookup._
import services.brute_force_prevention._
import services.dispose_service.{DisposeService, DisposeServiceImpl, DisposeWebService, DisposeWebServiceImpl}
import services.vehicle_lookup.{VehicleLookupService, VehicleLookupServiceImpl, VehicleLookupWebService, VehicleLookupWebServiceImpl}
import services.{DateService, DateServiceImpl, brute_force_prevention}
import utils.helpers._

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
    getProperty("addressLookupService.type", "ordnanceSurvey") match {
      case "ordnanceSurvey" =>
        bind[AddressLookupService].to[ordnance_survey.AddressLookupServiceImpl].asEagerSingleton()
        bind[AddressLookupWebService].to[ordnance_survey.WebServiceImpl].asEagerSingleton()
      case _ =>
        bind[AddressLookupService].to[gds.AddressLookupServiceImpl].asEagerSingleton()
        bind[AddressLookupWebService].to[gds.WebServiceImpl].asEagerSingleton()
    }
    bind[VehicleLookupWebService].to[VehicleLookupWebServiceImpl].asEagerSingleton()
    bind[VehicleLookupService].to[VehicleLookupServiceImpl].asEagerSingleton()
    bind[DisposeWebService].to[DisposeWebServiceImpl].asEagerSingleton()
    bind[DisposeService].to[DisposeServiceImpl].asEagerSingleton()
    bind[DateService].to[DateServiceImpl].asEagerSingleton()
    bind[CookieFlags].to[CookieFlagsFromConfig].asEagerSingleton()

    if (getProperty("encryptCookies", default = true)) {
      bind[CookieEncryption].toInstance(new AesEncryption with CookieEncryption)
      bind[CookieNameHashGenerator].toInstance(new Sha1HashGenerator with CookieNameHashGenerator)
      bind[ClientSideSessionFactory].to[EncryptedClientSideSessionFactory].asEagerSingleton()
    } else {
      bind[ClientSideSessionFactory].to[ClearTextClientSideSessionFactory].asEagerSingleton()
    }

    bind[BruteForcePreventionWebService].to[brute_force_prevention.WebServiceImpl].asEagerSingleton()
    bind[BruteForcePreventionService].to[BruteForcePreventionServiceImpl].asEagerSingleton()
    bind[LoggerLike].annotatedWith(Names.named(AccessLoggerName)).toInstance(Logger("dvla.common.AccessLogger"))
  }
}
