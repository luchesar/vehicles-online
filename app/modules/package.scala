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

  // Play.isTest will evaluate to true when you run "play test" from the command line
  // If play is being run to execute the tests then use the TestModule to provide fake 
  // implementations of traits otherwise use the DevModule to provide the real ones
  def module = if (Play.isTest) TestModule else DevModule

  lazy val injector = Guice.createInjector(module)

  /**
   * Provides real implementations for traits
   */
  object DevModule extends ScalaModule {
    def configure() {
      Logger.debug("Guice is loading DevModule")
      bind[V5CSearchWebService].to[V5cSearchResponseWebService]
      bind[LoginWebService].to[LoginServiceImpl]
    }
  }

  /**
   * Provides fake or test implementations for traits
   */
  object TestModule extends ScalaModule {

    /**
     * Fake implementation of the WebService trait with in memory data
     */
    case class FakeV5cSearchResponseWebService() extends V5CSearchWebService {
      override def invoke(cmd: V5cSearchModel): Future[V5cSearchResponse] = Future {
        V5cSearchResponse(true, "All ok ", V5cSearchConfirmationModel("vrn", "make", "model", "firstRegistered", "acquired"))
      }
    }

    /**
     * Fake implementation of the LoginWebService trait with in memory data
     */
    case class FakeLoginResponseWebService() extends LoginWebService {
      override def invoke(cmd: LoginPageModel): Future[LoginResponse] = Future {
        LoginResponse(true, "All ok ", LoginConfirmationModel("Roger", "Booth", "21/05/1977", Address(line1 = "115 Park Avenue", line4 = Some("United Kingdom"), postCode = "SA6 8HY")))
      }
    }

    /**
     * Bind the fake implementations the traits
     */
    def configure() {
      Logger.debug("Guice is loading TestModule")

      bind[V5CSearchWebService].to[FakeV5cSearchResponseWebService]
      bind[LoginWebService].to[FakeLoginResponseWebService]
    }
  }

}
