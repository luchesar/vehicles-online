package helpers.change_of_address

import models.domain.change_of_address.LoginConfirmationModel
import org.mockito.Mockito._
import play.api.Play.current
import mappings.change_of_address.LoginConfirmation
import models.domain.disposal_of_vehicle.AddressViewModel
import helpers.disposal_of_vehicle.BusinessChooseYourAddressPage._

object LoginCachePopulate {
  def setupCache() = {
    val address1 = AddressViewModel(address = Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
    val loginConfirmationModel = mock(classOf[LoginConfirmationModel])
    val key = LoginConfirmation.key

    when(loginConfirmationModel.firstName).thenReturn("mock firstName")
    when(loginConfirmationModel.surname).thenReturn("mock surname")
    when(loginConfirmationModel.address).thenReturn(address1)

    play.api.cache.Cache.set(key, loginConfirmationModel)
  }
}

