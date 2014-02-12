package mappings

import play.api.data.Mapping
import play.api.data.Forms._

object DropDown {
  def dropDown(dropDownOptions: Map[String, String]): Mapping[String] = {
    nonEmptyText(maxLength = 12) verifying constraints.DropDown.rules(dropDownOptions)
  }
}
