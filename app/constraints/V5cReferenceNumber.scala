package constraints


import play.api.data.Mapping
import play.api.data.Forms._
import constraints.NumberOnly._

object V5cReferenceNumber {
  def v5cReferenceNumber (minLength: Int = mappings.V5cReferenceNumber.minLength, maxLength: Int = mappings.V5cReferenceNumber.maxLength): Mapping[String] = {
    nonEmptyText(minLength, maxLength) verifying validNumberOnly
  }
}
