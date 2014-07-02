package mappings.common

import play.api.data.Mapping
import play.api.data.Forms.nonEmptyText

object DropDown {
  def addressDropDown: Mapping[String] = nonEmptyText
}