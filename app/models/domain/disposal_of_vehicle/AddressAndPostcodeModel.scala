package models.domain.disposal_of_vehicle

case class AddressAndPostcodeModel(addressLinesModel: AddressLinesModel, postcode: String) {
  // TODO add a UPRN parameter
  def toViewFormat() = s"${addressLinesModel.toViewFormat}, ${postcode}"
}