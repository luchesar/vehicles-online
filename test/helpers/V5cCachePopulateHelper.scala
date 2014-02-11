package helpers

import play.api.test.TestBrowser
import models.domain.change_of_address.{V5cSearchConfirmationModel, LoginConfirmationModel}
import org.scalatest.{Matchers, WordSpec}
import org.specs2.mock.Mockito
import play.api.Play.current
import models.domain.common.Address
import controllers.Mappings

object V5cCachePopulateHelper extends WordSpec with Matchers with Mockito {
  val v5cReferenceNumberValid = "12345678910"
  val vehicleVRNValid = "a1"
  val v5ckey = v5cReferenceNumberValid + "." + vehicleVRNValid

  def v5cCachePopulate() = {
    play.api.cache.Cache.set(Mappings.V5cReferenceNumber.key, v5cReferenceNumberValid)
    play.api.cache.Cache.set(Mappings.V5cRegistrationNumber.key, vehicleVRNValid)
    play.api.cache.Cache.set(v5ckey, V5cSearchConfirmationModel("a", "b", "c", "d", "e"))
  }
}