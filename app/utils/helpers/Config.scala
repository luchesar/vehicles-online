package utils.helpers

import app.ConfigProperties._

object Config {
  val microServiceUrlBase = getProperty("microServiceUrlBase", "http://localhost:8080")
}
