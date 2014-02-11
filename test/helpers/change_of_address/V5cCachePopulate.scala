package helpers.change_of_address

import models.domain.change_of_address.V5cSearchConfirmationModel
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import play.api.Play.current
import controllers.Mappings
import helpers.change_of_address.COAValidValues._

object V5cCachePopulate extends WordSpec with Matchers with Mockito {
  def v5cCachePopulate() = {
    play.api.cache.Cache.set(Mappings.V5cReferenceNumber.key, v5cDocumentReferenceNumberValid)
    play.api.cache.Cache.set(Mappings.V5cRegistrationNumber.key, v5cVehicleRegistrationNumberValid)
    play.api.cache.Cache.set(v5ckey, V5cSearchConfirmationModel("a", "b", "c", "d", "e"))
  }
}