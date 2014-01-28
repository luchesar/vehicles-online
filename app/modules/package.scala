package object modules {
  import com.tzavellas.sse.guice.ScalaModule
  import services._


  import app.ConfigProperties._
  import java.io.File
  import java.net.InetAddress
  import com.typesafe.config.ConfigFactory
  import java.util.UUID
  import org.slf4j.MDC
  import play.api._
  import play.api.Configuration
  import play.api.mvc._
  import play.api.mvc.Results._
  import play.api.Play.current
  import com.google.inject.Guice
  import scala.concurrent.{ExecutionContext, Future}
  import ExecutionContext.Implicits.global

  def module = if (Play.isTest) TestModule else DevModule

  lazy val injector = Guice.createInjector(module)


  object DevModule extends ScalaModule {
    def configure() {
      println("Guice is loading DevModule")
      bind[WebService].to[V5cSearchResponseWebService]
    }
  }

  object TestModule extends ScalaModule {
    def configure() {
      println("Guice is loading TestModule")
      //val fakeWebService = V5cSearchResponseWebService

      //bind[WebService].toInstance(fakeWebService)
      bind[WebService].to[V5cSearchResponseWebService]
    }
  }
}
