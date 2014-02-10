package modules

import app.DisposalOfVehicle.BusinessAddressSelect._
import com.tzavellas.sse.guice.ScalaModule
import services.{AddressLookupService, LoginWebService, V5cSearchWebService}
import scala.concurrent.{ExecutionContext, Future}
import play.api.Logger
import ExecutionContext.Implicits.global
import models.domain.change_of_address.V5cSearchConfirmationModel
import models.domain.change_of_address.V5cSearchResponse
import models.domain.change_of_address.LoginPageModel
import models.domain.change_of_address.LoginResponse
import models.domain.change_of_address.LoginConfirmationModel
import models.domain.change_of_address.V5cSearchModel
import models.domain.common.Address


/**
 * Provides fake or test implementations for traits
 */
object TestModule extends ScalaModule {

  /**
   * Fake implementation of the WebService trait with in memory data
   */
  case class FakeV5cSearchWebService() extends V5cSearchWebService {
    override def invoke(cmd: V5cSearchModel): Future[V5cSearchResponse] = Future {
      V5cSearchResponse(true, "All ok ", V5cSearchConfirmationModel("vrn", "make", "model", "firstRegistered", "acquired"))
    }
  }

  /**
   * Fake implementation of the LoginWebService trait with in memory data
   */
  case class FakeLoginWebService() extends LoginWebService {
    override def invoke(cmd: LoginPageModel): Future[LoginResponse] = Future {
      LoginResponse(true, "All ok ", LoginConfirmationModel("Roger", "Booth", "21/05/1977", Address(line1 = "115 Park Avenue", line4 = Some("United Kingdom"), postCode = "SA6 8HY")))
    }
  }

  /**
   * Fake implementation of the FakeAddressLookupService trait
   */
  case class FakeAddressLookupService() extends AddressLookupService {
    override def invoke(postcode: String): Map[String, String] = Map(
      "" -> "Please select",
      FirstAddress -> "This is the first option",
      SecondAddress -> "This is the second option"
    )
  }

  /**
   * Bind the fake implementations the traits
   */
  def configure() {
    Logger.debug("Guice is loading TestModule")

    bind[V5cSearchWebService].to[FakeV5cSearchWebService]
    bind[LoginWebService].to[FakeLoginWebService]
    bind[AddressLookupService].to[FakeAddressLookupService]
  }
}