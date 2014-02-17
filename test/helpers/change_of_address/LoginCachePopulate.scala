package helpers.change_of_address

import models.domain.change_of_address.LoginConfirmationModel
import org.mockito.Mockito._
import play.api.Play.current
import models.domain.common.Address
import mappings.change_of_address.LoginConfirmation

object LoginCachePopulate {
  def setupCache() = {
    val address = mock(classOf[Address])
    val loginConfirmationModel = mock(classOf[LoginConfirmationModel])
    val key = LoginConfirmation.key

    when(address.line1).thenReturn("mock line1")
    when(address.postCode).thenReturn("mock postcode")

    when(loginConfirmationModel.firstName).thenReturn("mock firstName")
    when(loginConfirmationModel.surname).thenReturn("mock surname")
    when(loginConfirmationModel.address).thenReturn(address)

    play.api.cache.Cache.set(key, loginConfirmationModel)
  }
}

