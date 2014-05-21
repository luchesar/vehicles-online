package services.fakes

import scala.concurrent.{ExecutionContext, Future}
import ExecutionContext.Implicits.global
import FakeVehicleLookupWebService._
import models.domain.disposal_of_vehicle._
import play.api.http.Status._
import play.api.libs.json.Json
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

  val vehicleDetailsNoResponse : (Int, Option[VehicleDetailsResponse]) = {
    (OK, None)
  }
}