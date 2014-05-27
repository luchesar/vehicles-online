package helpers

import org.scalatest.{Matchers, WordSpec}
import org.scalatest.mock.MockitoSugar
import org.scalatest.concurrent.ScalaFutures
import play.api.test.FakeApplication
import helpers.webbrowser.TestGlobal

abstract class UnitSpec extends WordSpec with Matchers with MockitoSugar with ScalaFutures