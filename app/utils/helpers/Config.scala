package utils.helpers

import app.ConfigProperties._
import scala.concurrent.duration._
import scala.language.postfixOps

object Config {
  // Micro-service config
  val vehicleLookupMicroServiceBaseUrl = getProperty("vehicleLookupMicroServiceUrlBase", "http://localhost:8090")
  val disposeVehicleMicroServiceBaseUrl = getProperty("disposeVehicleMicroServiceUrlBase", "http://localhost:8080")

  // Ordnance survey config
  val ordnanceSurveyMicroServiceUrl = getProperty("ordnancesurvey.ms.url", "")
  val ordnanceSurveyRequestTimeout = getProperty("ordnancesurvey.requesttimeout", "30000")

  // GDS address lookup config
  val gdsAddressLookupBaseUrl = getProperty("gdsaddresslookup.baseurl", "")
  val gdsAddressLookupAuthorisation = getProperty("gdsaddresslookup.authorisation", "")
  val gdsAddressLookupRequestTimeout = getProperty("gdsaddresslookup.requesttimeout", "30000")

  // Dispose
  val disposeMsRequestTimeout = getProperty("dispose.ms.requesttimeout", (30 seconds).toMillis)

  // Cookie expiry
  val cookieMaxAge: Int = getProperty("cookieMaxAge", (30 minutes).toSeconds.toInt)
}
