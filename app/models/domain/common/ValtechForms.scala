package models.domain.common

import play.api.data.Forms._
import play.api.data.Mapping
import play.api.data.validation.Constraints
import constraints.Text._

object ValtechForms  {
  // This can be empty
  def carersText(minLength: Int = 0, maxLength: Int = Int.MaxValue): Mapping[String] =
    text(minLength, maxLength) verifying restrictedStringText
}
