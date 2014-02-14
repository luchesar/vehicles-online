package mappings

import play.api.data.Mapping
import play.api.data.Forms._

object DropDown {
  def dropDown(dropDownOptions: Map[String, String]): Mapping[String] = {
    text(maxLength = 9999) verifying constraints.DropDown.rules(dropDownOptions) // TODO find the maxLength for address in GDS DB
  }
}
