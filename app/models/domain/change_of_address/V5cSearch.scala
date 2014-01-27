package models.domain

case class V5cSearchModel(V5cReferenceNumber: String, vehicleVRN: String)

case class V5cSearchConfirmationModel(vrn: String, make: String, model: String, firstRegistered: String, acquired: String)
