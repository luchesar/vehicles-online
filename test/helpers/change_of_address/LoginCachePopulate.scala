package helpers.change_of_address

import models.domain.change_of_address.LoginConfirmationModel
import org.mockito.Mockito._
import play.api.Play.current
import mappings.change_of_address.LoginConfirmation
import models.domain.disposal_of_vehicle.AddressAndPostcodeModel
import helpers.disposal_of_vehicle.BusinessChooseYourAddressPage._

object LoginCachePopulate {
  def setupCache() = {
    val address = mock(classOf[AddressAndPostcodeModel])
    val loginConfirmationModel = mock(classOf[LoginConfirmationModel])
    val key = LoginConfirmation.key

    when(address.addressLinesModel).thenReturn(address1.addressLinesModel)
    when(address.postcode).thenReturn(address1.postcode)

    when(loginConfirmationModel.firstName).thenReturn("mock firstName")
    when(loginConfirmationModel.surname).thenReturn("mock surname")
    when(loginConfirmationModel.address).thenReturn(address)

    play.api.cache.Cache.set(key, loginConfirmationModel)
  }
}

