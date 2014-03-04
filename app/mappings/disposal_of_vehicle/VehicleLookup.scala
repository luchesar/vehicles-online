package mappings.disposal_of_vehicle

/**
 * Set of mapping ids to use when handling the posted form data in the vehicle lookup page
 * Also includes the cache key to use when storing the vehicle details
 */
object VehicleLookup {
  val v5cReferenceNumberId = "v5cReferenceNumber"
  val v5cRegistrationNumberId = "v5cRegistrationNumber"
  val v5cKeeperNameId = "v5cKeeperName"
  val v5cPostcodeId = "v5cPostcode"
  val vehicleLookupDetailsCacheKey = "vehicleDetails"
  val vehicleLookupFormModelCacheKey = "vehicleLookupFormModel"
}
