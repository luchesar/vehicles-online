package services.ordnance_survey.domain

import java.net.URI
import play.api.libs.json._
import com.fasterxml.jackson.annotation.JsonValue

case class OSAddressbaseHeader(
                                uri: URI,
                                query: String,
                                offset: Int,
                                totalresults: Int,
                                format: String,
                                dataset: String,
                                maxresults: Int
                                )

object OSAddressbaseHeader {
  implicit val uriReads: Reads[URI] = new Reads[URI] {
    override def reads(json: JsValue) = JsSuccess(new URI(json.as[String]))
  }
  implicit val uriWrites: Writes[URI] = new Writes[URI] {
    override def writes(uri: URI) = JsString(uri.toString)
  }

  implicit val readsOSAddressbaseHeader = Json.format[OSAddressbaseHeader]
}
