package controllers.change_of_address

import play.api.test.{FakeRequest, WithApplication}
import play.api.test.Helpers._
import org.scalatest.{Matchers, WordSpec}
import models.domain.change_of_address.LoginPageModel
import org.mockito.Mockito._
import org.mockito.Matchers._
import modules.TestModule.FakeLoginWebService
import org.scalatest.mock.MockitoSugar
import helpers.change_of_address.{LoginConfirmationPage, LoginPagePopulate}

class LoginPageControllerSpec extends WordSpec with Matchers with MockitoSugar {
  "LoginPage - Controller" should {
    val mockLoginPageModel = mock[LoginPageModel]
    val mockWebService = mock[services.LoginWebService]
    when(mockWebService.invoke(any[LoginPageModel])).thenReturn(FakeLoginWebService().invoke(mockLoginPageModel))
    val loginPage = new controllers.change_of_address.LoginPage(mockWebService)


    "present" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()

      // Act
      val result = loginPage.present(request)

      // Assert
      status(result) should equal(OK)
    }


    "redirect to next page after the next button is clicked" in new WithApplication {
      // Arrange
      val request = FakeRequest().withSession()
        .withFormUrlEncodedBody(mappings.change_of_address.LoginPage.usernameId -> LoginPagePopulate.usernameValid,
          mappings.change_of_address.LoginPage.passwordId -> LoginPagePopulate.passwordValid)

      // Act
      val result = loginPage.submit(request)

      // Assert
      status(result) should equal(SEE_OTHER)
      redirectLocation(result) should equal(Some(LoginConfirmationPage.url))
    }
  }
}