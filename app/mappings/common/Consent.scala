package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._

object Consent {

  def consent: Mapping[String] = nonEmptyText

}

