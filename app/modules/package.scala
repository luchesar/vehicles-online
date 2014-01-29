package object modules {

  import com.tzavellas.sse.guice.ScalaModule
  import services._
  import play.api._
  import play.api.Play.current
  import com.google.inject.Guice
  import models.domain.change_of_address.{V5cSearchConfirmationModel, V5cSearchResponse, V5cSearchModel}
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext
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

    case class FakeV5cSearchResponseWebService() extends WebService {
      override def invoke(cmd: V5cSearchModel): Future[V5cSearchResponse] = Future {
        V5cSearchResponse(true, "All ok ", V5cSearchConfirmationModel("vrn", "make", "model", "firstRegistered", "acquired"))
      }
    }


    def configure() {
      println("Guice is loading TestModule")

      bind[WebService].to[FakeV5cSearchResponseWebService]
    }
  }

}
