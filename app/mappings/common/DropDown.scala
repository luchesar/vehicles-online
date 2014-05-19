package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._
import constraints.common.DropDown.validDropDown

object DropDown {
  def addressDropDown: Mapping[Long] = longNumber

/*  def addressDropDown(dropDownOptions: Map[String, String]): Mapping[String] = {
    longNumber verifying validDropDown(dropDownOptions)
  }*/
}
