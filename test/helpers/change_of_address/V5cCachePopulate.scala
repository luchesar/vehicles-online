package helpers.change_of_address

import models.domain.change_of_address.V5cSearchConfirmationModel
import play.api.Play.current
import helpers.change_of_address.Helper._
import mappings.common.{ReferenceNumber, RegistrationNumber}

object V5cCachePopulate {
  def happyPath() = {
    play.api.cache.Cache.set(ReferenceNumber.key, v5cDocumentReferenceNumberValid)
    play.api.cache.Cache.set(RegistrationNumber.key, v5cVehicleRegistrationNumberValid)
    play.api.cache.Cache.set(v5ckey, V5cSearchConfirmationModel("a", "b", "c", "d", "e"))
  }
}