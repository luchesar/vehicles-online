package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import org.scalatest.{Matchers, WordSpec}
import models.domain.change_of_address.LoginPageModel
import org.mockito.Mockito._
import org.mockito.Matchers._
import org.scalatest.mock.MockitoSugar
import helpers.change_of_address.LoginConfirmationPage
import helpers.change_of_address.Helper._
import services.fakes.FakeLoginWebService

class LoginPageControllerSpec extends WordSpec with Matchers with MockitoSugar {
  "LoginPage - Controller" should {
    val mockLoginPageModel = mock[LoginPageModel]
    val mockWebService = mock[services.LoginWebService]
    when(mockWebService.invoke(any[LoginPageModel])).thenReturn(new FakeLoginWebService().invoke(mockLoginPageModel))
    val loginPage = new controllers.change_of_address.LoginPage(mockWebService)

    "present" in new WithApplication {
      val request = FakeRequest().withSession()

      val result = loginPage.present(request)

      status(result) should equal(OK)
    }

    "redirect to next page after the next button is clicked" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(mappings.change_of_address.LoginPage.usernameId -> usernameValid,
          mappings.change_of_address.LoginPage.passwordId -> passwordValid)

      val result = loginPage.submit(request)

      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal(Some(LoginConfirmationPage.url))
    }

    "report bad request when no details are filled in" in new WithApplication {
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody()

      val result = loginPage.submit(request)

      status(result) should equal(BAD_REQUEST)
    }
  }
}