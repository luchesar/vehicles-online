package services.vehicle_lookup

import com.github.tomakehurst.wiremock.client.WireMock._
import common.ClientSideSessionFactory
import helpers.{UnitSpec, WireMockFixture}
import models.domain.disposal_of_vehicle.VehicleDetailsRequest
import play.api.libs.json._
import services.HttpHeaders
import utils.helpers.Config

class VehicleLookupWebServiceImplSpec  extends UnitSpec  with WireMockFixture {
  import composition.TestComposition.{testInjector => injector}

  implicit val clientSideSessionFactory = injector.getInstance(classOf[ClientSideSessionFactory])
  val lookupService = new VehicleLookupWebServiceImpl(new Config() {
    override val vehicleLookupMicroServiceBaseUrl = s"http://localhost:$wireMockPort"
  })

  val trackingId = "track-id-test"

  val request = VehicleDetailsRequest(
    referenceNumber = "ref number",
    registrationNumber = "reg number",
    userName = "user name"
  )

  implicit val vehiclesDetailsFormat = Json.format[VehicleDetailsRequest]

  "callDisposeService" ignore {

    "send the serialised json request" in {
      val resultFuture = lookupService.callVehicleLookupService(request, trackingId)
      whenReady(resultFuture) { result =>
        wireMock.verifyThat(1, postRequestedFor(
          urlEqualTo(s"/vehicles/lookup/v1/dispose")
        ).withHeader(HttpHeaders.TrackingId, equalTo(trackingId)).
          withRequestBody(equalTo(Json.toJson(request).toString())))
      }
    }
  }
}
