package mappings

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.DropDown._

object DropDown {
  def dropDown(dropDownOptions: Map[String, String]): Mapping[String] = {
    nonEmptyText(maxLength = 12) verifying validDropDown(dropDownOptions)
  }
}
