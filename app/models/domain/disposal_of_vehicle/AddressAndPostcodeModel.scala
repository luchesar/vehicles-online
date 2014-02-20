package models.domain.disposal_of_vehicle

case class AddressAndPostcodeModel(addressLinesModel: AddressLinesModel, postcode: String) {
  def toViewFormat() = s"${addressLinesModel.toViewFormat}, ${Some(postcode)}"
}