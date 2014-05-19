package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.common.DropDown.validDropDown

object DropDown {
  def dropDown: Mapping[Long] = longNumber

/*  def dropDown(dropDownOptions: Map[String, String]): Mapping[String] = {
    nonEmptyText(maxLength = 9999) verifying validDropDown(dropDownOptions)
  }*/
}
