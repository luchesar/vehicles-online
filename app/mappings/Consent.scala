package mappings

import play.api.data.Mapping
import play.api.data.Forms._

object Consent {
  def consent: Mapping[Boolean] = {
    boolean verifying constraints.Consent.rules
  }
}
