package models.domain.disposal_of_vehicle

case class DisposeModel(vehicleMake: String, vehicleModel: String, keeperName: String, keeperAddress: AddressViewModel, dealerName: String, dealerAddress: AddressViewModel)
