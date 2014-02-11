package helpers.change_of_address

import models.domain.change_of_address.LoginConfirmationModel
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import play.api.Play.current
import models.domain.common.Address
import controllers.Mappings

object LoginCachePopulate extends WordSpec with Matchers with Mockito {
  def loginCachePopulate() = {
    val address = mock[Address]
    val loginConfirmationModel = mock[LoginConfirmationModel]
    val key = Mappings.LoginConfirmationModel.key

    address.line1 returns "mock line1"
    address.postCode returns "mock postcode"

    loginConfirmationModel.firstName returns "mock firstName"
    loginConfirmationModel.surname returns "mock surname"
    loginConfirmationModel.address returns address

    play.api.cache.Cache.set(key, loginConfirmationModel)
  }



}