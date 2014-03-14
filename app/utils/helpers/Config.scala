package utils.helpers

import app.ConfigProperties._

object Config {
  // Micro service config
  val microServiceBaseUrl = getProperty("microServiceUrlBase", "http://localhost:8080")

  // Ordnance survey config
  val ordnanceSurveyUsername = getProperty("ordnancesurvey.username", "")
  val ordnanceSurveyPassword = getProperty("ordnancesurvey.password", "")
  val ordnanceSurveyBaseUrl = getProperty("ordnancesurvey.baseurl", "")

  // GDS address lookup config
  val gdsAddressLookupBaseUrl = getProperty("gdsaddresslookup.baseurl", "http://localhost:8081") // TODO change the default to match the micro-service.
}
