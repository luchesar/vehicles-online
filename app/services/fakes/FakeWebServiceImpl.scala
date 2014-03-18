package services.fakes

import play.api.libs.ws.WS
import services.WebService

class FakeWebServiceImpl extends WebService {
   override def url(url: String): WS.WSRequestHolder = WS.url(url) // TODO need to replace this with a way of stubbing different status codes.
 }
