package utils.helpers

import app.ConfigProperties._

object Environment {
  val microServiceUrlBase = getProperty("microServiceUrlBase", "http://localhost:8080")
}
