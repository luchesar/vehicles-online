package models.domain.disposal_of_vehicle

import models.domain.common.Address

case class VehicleDetailsModel(vehicleMake: String, vehicleModel: String, keeperName: String, keeperAddress: Address)