package services.fakes

import scala.xml.Elem
import play.api.libs.json.JsValue
import play.api.libs.ws.Response

class FakeResponse(
                    override val status: Int,
                    override val statusText: String = "",
                    headers: Map[String, String] = Map.empty,
                    fakeBody: Option[String] = None,
                    fakeXml: Option[Elem] = None,
                    fakeJson: Option[JsValue] = None) extends Response(null) {

  override def getAHCResponse = throw new NotImplementedError("getAHCResponse is not available on a fake response")
  override def header(key: String): Option[String] = headers.get(key)

  override lazy val body: String = fakeBody.getOrElse("")
  override lazy val xml: Elem = fakeXml.get
  override lazy val json: JsValue = fakeJson.get
}

object FakeResponse {

  def apply(status: Int,
            statusText: String = "",
            headers: Map[String, String] = Map.empty,
            fakeBody: Option[String] = None,
            fakeXml: Option[Elem] = None,
            fakeJson: Option[JsValue] = None) =
    new FakeResponse(status, statusText, headers, fakeBody, fakeXml, fakeJson)
}