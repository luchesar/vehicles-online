package mappings.common

import play.api.data.Mapping
import play.api.data.Forms._

object DropDown {
  def addressDropDown: Mapping[String] = nonEmptyText

/*  def addressDropDown(dropDownOptions: Map[String, String]): Mapping[String] = {
    longNumber verifying validDropDown(dropDownOptions)
  }*/
}
