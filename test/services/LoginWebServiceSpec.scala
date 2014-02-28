package services

import org.scalatest.{Matchers, WordSpec}
import org.scalatest.mock.MockitoSugar
import play.api.libs.ws.Response
import models.domain.change_of_address.{LoginConfirmationModel, LoginResponse, LoginPageModel}
import models.domain.disposal_of_vehicle.AddressViewModel
import play.api.libs.json.Json
import org.mockito.Mockito._
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global

class LoginWebServiceSpec extends WordSpec with Matchers with MockitoSugar {
  "invoke" should {

    // This class allows overriding of the base classes methods which call the real web service.
    class PartialLoginWebServiceImpl(
                                      responseOfLoginWebService: Future[Response] = Future {
                                        mock[Response]
                                      }
                                      ) extends LoginWebServiceImpl() {
      override def callWebService(cmd: LoginPageModel): Future[Response] = responseOfLoginWebService
    }

    "return a LoginResponse" in {
      val address = AddressViewModel(address = Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
      val loginConfirmationModel = LoginConfirmationModel(firstName = "stub firstName", surname = "stub surname", dob = "stub dob", address = address)
      val loginResponse = LoginResponse(
        success = true,
        message = "stub message",
        loginConfirmationModel = loginConfirmationModel
      )


      val inputAsJson = Json.toJson(loginResponse)


      val response = mock[Response]
      when(response.json).thenReturn(inputAsJson)

      val loginPageModel = mock[LoginPageModel]

      val loginWebServiceImpl = new PartialLoginWebServiceImpl(
        responseOfLoginWebService = Future {
          response
        }
      )

      // Act
      val result = loginWebServiceImpl.invoke(loginPageModel)

      result.map { r => r should equal(loginResponse)}
    }
  }
}
