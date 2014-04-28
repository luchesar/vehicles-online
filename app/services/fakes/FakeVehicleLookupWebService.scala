package services.fakes

import models.domain.disposal_of_vehicle._
import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import models.domain.disposal_of_vehicle.VehicleDetailsResponse
import services.vehicle_lookup.VehicleLookupWebService
import play.api.libs.json.Json
import play.api.Logger
import FakeVehicleLookupWebService._
import play.api.http.Status._
import scala.Some


class FakeVehicleLookupWebService extends VehicleLookupWebService {
  override def callVehicleLookupService(request: VehicleDetailsRequest) = Future {
    val vehicleDetailsResponse = {
      request.referenceNumber match {
        case "99999999991" => vehicleDetailsResponseVRMNotFound._2
        case "99999999992" => vehicleDetailsResponseDocRefNumberNotLatest._2
        case "99999999999" => vehicleDetailsResponseNotFoundResponseCode._2
        case _ => vehicleDetailsResponseSuccess._2
      }
    }
    val responseAsJson = Json.toJson(vehicleDetailsResponse)
    //Logger.debug(s"FakeVehicleLookupWebService callVehicleLookupService with: $responseAsJson")
    new FakeResponse(status = OK, fakeJson = Some(responseAsJson)) // Any call to a webservice will always return this successful response.
  }
}

object FakeVehicleLookupWebService {
  val registrationNumberValid = "AB12AWR"
  val registrationNumberWithSpaceValid = "AB12 AWR"
  val referenceNumberValid = "12345678910"
  val vehicleMakeValid = "Alfa Romeo"
  val vehicleModelValid = "Alfasud ti"
  val keeperNameValid = "Keeper Name"
  val keeperUprnValid = 10123456789L
  val consentValid = "true"

  private val vehicleDetails = VehicleDetailsDto(registrationNumber = registrationNumberValid,
    vehicleMake = vehicleMakeValid,
    vehicleModel = vehicleModelValid)

  val vehicleDetailsResponseSuccess : (Int, Option[VehicleDetailsResponse]) = {
    (OK, Some(VehicleDetailsResponse(responseCode = None, vehicleDetailsDto = Some(vehicleDetails))))
  }

  val vehicleDetailsResponseVRMNotFound : (Int, Option[VehicleDetailsResponse]) = {
    (OK, Some(VehicleDetailsResponse(responseCode = Some("vehicle_lookup_vrm_not_found"),vehicleDetailsDto = None)))
  }

  val vehicleDetailsResponseDocRefNumberNotLatest : (Int, Option[VehicleDetailsResponse]) = {
    (OK, Some(VehicleDetailsResponse(responseCode = Some("vehicle_lookup_document_record_mismatch"), vehicleDetailsDto = None)))
  }

  val vehicleDetailsResponseNotFoundResponseCode : (Int, Option[VehicleDetailsResponse]) = {
    (OK,Some(VehicleDetailsResponse(responseCode = None, vehicleDetailsDto = None)))
  }

  val vehicleDetailsServerDown : (Int, Option[VehicleDetailsResponse]) = {
    (SERVICE_UNAVAILABLE, None)
  }
}