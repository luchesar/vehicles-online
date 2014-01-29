import models.domain.change_of_address.Address
import models.domain.change_of_address.LoginConfirmationModel
import models.domain.change_of_address.LoginPageModel

package object modules {

  import com.tzavellas.sse.guice.ScalaModule
  import services._
  import play.api._
  import play.api.Play.current
  import com.google.inject.Guice
  import models.domain.change_of_address.{V5cSearchConfirmationModel, V5cSearchResponse, V5cSearchModel}
  import models.domain.change_of_address.{LoginConfirmationModel, LoginResponse, LoginPageModel}
  import scala.concurrent.Future
  import scala.concurrent.ExecutionContext
  import ExecutionContext.Implicits.global

  def module = if (Play.isTest) TestModule else DevModule

  lazy val injector = Guice.createInjector(module)


  object DevModule extends ScalaModule {
    def configure() {
      println("Guice is loading DevModule")
      bind[WebService].to[V5cSearchResponseWebService]
      bind[LoginWebService].to[LoginServiceImpl]
    }
  }

  object TestModule extends ScalaModule {

    case class FakeV5cSearchResponseWebService() extends WebService {
      override def invoke(cmd: V5cSearchModel): Future[V5cSearchResponse] = Future {
        V5cSearchResponse(true, "All ok ", V5cSearchConfirmationModel("vrn", "make", "model", "firstRegistered", "acquired"))
      }
    }

    case class FakeLoginResponseWebService() extends LoginWebService {
      override def invoke(cmd: LoginPageModel): Future[LoginResponse] = Future {
        LoginResponse(true, "All ok ", LoginConfirmationModel("Roger", "Booth", "21/05/1977", Address(line1 = "115 Park Avenue", line2 = None, line3 = None, line4 = Some("United Kingdom"), postCode = "SA6 8HY")))
      }
    }

    def configure() {
      println("Guice is loading TestModule")

      bind[WebService].to[FakeV5cSearchResponseWebService]
      bind[LoginWebService].to[FakeLoginResponseWebService]
    }
  }

}
