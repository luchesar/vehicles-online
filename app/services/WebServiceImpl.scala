package services

import play.api.libs.ws.WS

class WebServiceImpl extends WebService {
  override def url(url: String): WS.WSRequestHolder = WS.url(url)
}
