package mappings.common

import play.api.data.Mapping
import play.api.data.Forms.nonEmptyText

object Consent {
  def consent: Mapping[String] = nonEmptyText
}