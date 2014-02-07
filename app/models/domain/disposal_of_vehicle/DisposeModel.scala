package models.domain.disposal_of_vehicle

import models.domain.common.Address

case class DisposeModel(vehicleMake: String, vehicleModel: String, keeperName: String, keeperAddress: Address, dealerName: String, dealerAddress: Address)
