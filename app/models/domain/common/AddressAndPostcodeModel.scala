package models.domain.common

case class AddressAndPostcodeModel(uprn: Option[Int] = None, addressLinesModel: AddressLinesModel, postcode: String) {
  def toViewFormat: Seq[String] = addressLinesModel.toViewFormat :+ postcode
}