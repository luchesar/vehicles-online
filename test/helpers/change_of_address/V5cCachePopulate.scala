package helpers.change_of_address

import models.domain.change_of_address.V5cSearchConfirmationModel
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import play.api.Play.current
import helpers.change_of_address.Helper._
import mappings.{V5cRegistrationNumber, V5cReferenceNumber}

object V5cCachePopulate {
  def happyPath() = {
    play.api.cache.Cache.set(V5cReferenceNumber.key, v5cDocumentReferenceNumberValid)
    play.api.cache.Cache.set(V5cRegistrationNumber.key, v5cVehicleRegistrationNumberValid)
    play.api.cache.Cache.set(v5ckey, V5cSearchConfirmationModel("a", "b", "c", "d", "e"))
  }
}