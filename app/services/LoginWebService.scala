package services

import models.domain.change_of_address._
import scala.concurrent.Future
import models.domain.change_of_address.LoginPageModel

/**
 * Defines the LoginWebService 
 */
trait LoginWebService {
  def invoke(cmd: LoginPageModel): Future[LoginResponse]
}
