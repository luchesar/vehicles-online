package services.fakes

import FakeVehicleLookupWebService._
import models.domain.disposal_of_vehicle._
import play.api.http.Status._
import play.api.libs.json.Json
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import services.vehicle_lookup.VehicleLookupWebService

final class FakeVehicleLookupWebService extends VehicleLookupWebService {
  override def callVehicleLookupService(request: VehicleDetailsRequest) = Future {
    val (responseStatus, response) = {
      request.referenceNumber match {
        case "99999999991" => vehicleDetailsResponseVRMNotFound
        case "99999999992" => vehicleDetailsResponseDocRefNumberNotLatest
        case "99999999999" => vehicleDetailsResponseNotFoundResponseCode
        case _ => vehicleDetailsResponseSuccess
      }
    }
    val responseAsJson = Json.toJson(response)
    //Logger.debug(s"FakeVehicleLookupWebService callVehicleLookupService with: $responseAsJson")
    new FakeResponse(status = responseStatus, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
  }
}

object FakeVehicleLookupWebService {
  final val RegistrationNumberValid = "AB12AWR"
  final val RegistrationNumberWithSpaceValid = "AB12 AWR"
  final val ReferenceNumberValid = "12345678910"
  final val VehicleMakeValid = "Alfa Romeo"
  final val VehicleModelValid = "Alfasud ti"
  final val KeeperNameValid = "Keeper Name"
  final val KeeperUprnValid = 10123456789L
  final val ConsentValid = "true"

  private val vehicleDetails = VehicleDetailsDto(registrationNumber = RegistrationNumberValid,
    vehicleMake = VehicleMakeValid,
    vehicleModel = VehicleModelValid)

  val vehicleDetailsResponseSuccess: (Int, Option[VehicleDetailsResponse]) = {
    (OK, Some(VehicleDetailsResponse(responseCode = None, vehicleDetailsDto = Some(vehicleDetails))))
  }

  val vehicleDetailsResponseVRMNotFound: (Int, Option[VehicleDetailsResponse]) = {
    (OK, Some(VehicleDetailsResponse(responseCode = Some("vehicle_lookup_vrm_not_found"), vehicleDetailsDto = None)))
  }

  val vehicleDetailsResponseDocRefNumberNotLatest: (Int, Option[VehicleDetailsResponse]) = {
    (OK, Some(VehicleDetailsResponse(responseCode = Some("vehicle_lookup_document_record_mismatch"), vehicleDetailsDto = None)))
  }

  val vehicleDetailsResponseNotFoundResponseCode: (Int, Option[VehicleDetailsResponse]) = {
    (OK, Some(VehicleDetailsResponse(responseCode = None, vehicleDetailsDto = None)))
  }

  val vehicleDetailsServerDown: (Int, Option[VehicleDetailsResponse]) = {
    (SERVICE_UNAVAILABLE, None)
  }

  val vehicleDetailsNoResponse: (Int, Option[VehicleDetailsResponse]) = {
    (OK, None)
  }
}