package models

import org.scalatest.{Matchers, WordSpec}
import scala.language.postfixOps
import uk.gov.gds.locality.api.models.PlacesAddress
import play.api.libs.json.Json
import uk.gov.gds.locality.api.models.JsonFormats._
import com.mongodb.casbah.Imports._
import com.novus.salat._
import com.novus.salat.global._

class GdsPlaceAddressSpec extends WordSpec with Matchers {
  "PlacesAddress" should {
    val lineOne: String = "a"
    val lineTwo: String = "b"
    val lineThree: String = "c"
    val lineFour: String = "d"
    val lineFive: String = "e"
    val county: String = "f"
    val city: String = "g"
    val postcode: String = "h"
    val uprn: String = "i"

    val plainOldScalaObject = PlacesAddress(
      lineOne,
      lineTwo,
      lineThree,
      lineFour,
      lineFive,
      county,
      city,
      postcode,
      uprn
    )

    val plainOldMongoDBObject = MongoDBObject(
      "lineOne" -> lineOne,
      "lineTwo" -> lineTwo,
      "lineThree" -> lineThree,
      "lineFour" -> lineFour,
      "lineFive" -> lineFive,
      "county" -> county,
      "city" -> city,
      "postcode" -> postcode,
      "uprn" -> uprn
    )

    "serialize to MongoDBObject" in {
      // Act
      val result = grater[PlacesAddress].asDBObject(plainOldScalaObject) // Using "salat" to do the the conversion to MongoDB's BSON flavour of JSON.

      result should equal(plainOldMongoDBObject)
    }

    "deserialize from MongoDBObject" in {
      // Act
      val result = grater[PlacesAddress].asObject(plainOldMongoDBObject)

      result should equal(plainOldScalaObject)
    }
  }
}