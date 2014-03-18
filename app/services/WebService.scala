package services

// Wrapper around our webservice call so that we can inject mock versions or real version (which is a play object without a trait!)
trait WebService {
  def url(url: String): play.api.libs.ws.WS.WSRequestHolder
}
