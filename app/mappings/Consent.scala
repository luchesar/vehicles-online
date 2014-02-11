package mappings

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.Consent._

object Consent {
  def consent: Mapping[Boolean] = {
    boolean verifying validConsent
  }
}
