package models.domain.common

import mappings.common.AddressLines._
import play.api.libs.json.Json

case class AddressLinesModel(line1: String,
                             line2: Option[String] = None,
                             line3: Option[String] = None,
                             line4: Option[String] = None) {
  def toViewFormat: Seq[String] = Seq(Some(line1), line2, line3, line4).flatten

  def totalCharacters = toViewFormat.map(_.length).sum // TODO test this in isolation in a model test
}

object AddressLinesModel {
  implicit val addressLinesModelFormat = Json.format[AddressLinesModel]
  implicit val cacheKey = CacheKey[AddressLinesModel](addressLinesCacheKey)
}