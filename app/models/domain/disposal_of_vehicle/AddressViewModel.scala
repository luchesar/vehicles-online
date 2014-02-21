package models.domain.disposal_of_vehicle

case class AddressViewModel(address: Seq[String])

object AddressViewModel{
  def from(address: AddressAndPostcodeModel): AddressViewModel = {
    AddressViewModel(address = address.toViewFormat)
  }
}