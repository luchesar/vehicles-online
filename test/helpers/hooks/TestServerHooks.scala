package helpers.hooks

import cucumber.api.java.After
import cucumber.api.java.Before
import play.api.test.TestServer
import play.api.test.FakeApplication

final class TestServerHooks {

  val port: Int = 9001
  val app: FakeApplication = FakeApplication()
  val testServer: TestServer = TestServer(port, app)

  @Before(order = 500)
  def startServer() = {
    testServer.start()
  }

  @After(order = 500)
  def stopServer() = {
    testServer.stop()
  }
}