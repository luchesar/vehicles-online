package mappings.disposal_of_vehicle

/**
 * Set of mapping ids to use when handling the posted form data in the vehicle lookup page
 * Also includes the cache key to use when storing the vehicle details
 */
object VehicleLookup {
  val referenceNumberId = "referenceNumber"
  val registrationNumberId = "registrationNumber"
  val consentId = "consent"
  val vehicleLookupDetailsCacheKey = "vehicleDetails"
  val vehicleLookupFormModelCacheKey = "vehicleLookupFormModel"
}
