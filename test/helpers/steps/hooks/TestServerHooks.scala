package helpers.steps.hooks

import cucumber.api.java.After
import cucumber.api.java.Before
import play.api.test.TestServer
import play.api.test.FakeApplication
import helpers.webbrowser.TestGlobal
import TestServerHooks._

final class TestServerHooks {
  private val testServer: TestServer = TestServer(port = port, application = fakeAppWithTestGlobal)

  @Before(order = 500)
  def startServer() = {
    testServer.start()
  }

  @After(order = 500)
  def stopServer() = {
    testServer.stop()
  }
}

object TestServerHooks {
  private final val port: Int = 9001
  private lazy val fakeAppWithTestGlobal: FakeApplication = FakeApplication(withGlobal = Some(TestGlobal))
}