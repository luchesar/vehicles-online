package views.disposal_of_vehicle

import helpers.UiSpec
import helpers.webbrowser.{TestHarness, WebDriverFactory}
import org.apache.http.client.methods._
import org.apache.http.impl.client.HttpClients
import play.mvc.Http.Status

class HealthCheckIntegrationSpec extends UiSpec with TestHarness {
  "Accessing the /healthcheck url" should {
    "return 200 for GET and POST" in new WebBrowser {
      var httpResponse = execute(new HttpGet(WebDriverFactory.testUrl + "/healthcheck"))
      try httpResponse.getStatusLine.getStatusCode should be(Status.OK)
      finally httpResponse.close()

      httpResponse = execute(new HttpPost(WebDriverFactory.testUrl + "/healthcheck"))
      try httpResponse.getStatusLine.getStatusCode should be (Status.OK)
      finally httpResponse.close()
    }

    "return 404 for PUT etc." in new WebBrowser {
      val httpResponse = execute(new HttpPut(WebDriverFactory.testUrl + "/healthcheck"))
      try httpResponse.getStatusLine.getStatusCode should be(Status.NOT_FOUND)
      finally httpResponse.close()
    }
  }

  private def execute(method: HttpRequestBase): CloseableHttpResponse = {
    val httpClient = HttpClients.createDefault()
    httpClient.execute(method)
  }
}
