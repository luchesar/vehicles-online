package utils.helpers

import app.ConfigProperties._
import scala.concurrent.duration._
import scala.language.postfixOps

object Config {
  // Micro-service config
  val vehicleLookupMicroServiceBaseUrl: String = getProperty("vehicleLookupMicroServiceUrlBase", "NOT FOUND")
  val disposeVehicleMicroServiceBaseUrl: String = getProperty("disposeVehicleMicroServiceUrlBase", "NOT FOUND")

  // Ordnance survey config
  val ordnanceSurveyMicroServiceUrl: String = getProperty("ordnancesurvey.ms.url", "NOT FOUND")
  val ordnanceSurveyRequestTimeout: Int = getProperty("ordnancesurvey.requesttimeout", (5 seconds).toMillis.toInt)

  // GDS address lookup config
  val gdsAddressLookupBaseUrl: String = getProperty("gdsaddresslookup.baseurl", "")
  val gdsAddressLookupAuthorisation: String = getProperty("gdsaddresslookup.authorisation", "")
  val gdsAddressLookupRequestTimeout: Int = getProperty("gdsaddresslookup.requesttimeout", (5 seconds).toMillis.toInt)

  // Dispose
  val disposeMsRequestTimeout: Int = getProperty("dispose.ms.requesttimeout", (5 seconds).toMillis.toInt)

  // Brute force prevention config
  val bruteForcePreventionMicroServiceBaseUrl: String = getProperty("bruteForcePreventionMicroServiceBase", "NOT FOUND")
  val bruteForcePreventionTimeout: Int = getProperty("bruteForcePrevention.requesttimeout", (5 seconds).toMillis.toInt)
  val bruteForcePreventionEnabled: Boolean = getProperty("bruteForcePrevention.enabled", default = true)
}
