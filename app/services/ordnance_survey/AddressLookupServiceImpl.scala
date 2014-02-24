package services.ordnance_survey

import services.AddressLookupService
import models.domain.disposal_of_vehicle.AddressViewModel
import utils.helpers.Config
import play.api.Logger
import play.api.libs.ws.WS
import com.ning.http.client.Realm.AuthScheme
import services.ordnance_survey.domain.OSAddressbaseSearchResponse
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global


class AddressLookupServiceImpl extends AddressLookupService {
  val address1 = AddressViewModel(uprn = Some(1234), address = Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
  val address2 = AddressViewModel(uprn = Some(4567), address = Seq("Penarth Road", "Cardiff", "CF11 8TT"))

  override def fetchAddressesForPostcode(postcode: String): Future[Map[String, String]] = {
    val username = s"${Config.ordnanceSurveyUsername}"
    val password = s"${Config.ordnanceSurveyPassword}"
    val baseUrl = s"${Config.ordnanceSurveyBaseUrl}"

    val endPoint = s"${baseUrl}/postcode?postcode=${postcode}&dataset=dpa" // TODO add lpi to URL, but need to set orgnaisation as Option on the type.
    Logger.debug(s"Calling Ordnance Survey postcode lookup service on ${endPoint}...")
    val futureOfResponse = WS.url(endPoint).withAuth(username = username, password = password, scheme = AuthScheme.BASIC).get()

    futureOfResponse.map {
      resp =>
        Logger.debug(s"Http response code from Ordnance Survey postcode lookup service was: ${resp.status}")
        val body = resp.json.as[OSAddressbaseSearchResponse]

        Logger.debug(s"totalresults ${body.header.totalresults}")


        Map(
          address1.uprn.getOrElse(1234).toString -> address1.address.mkString(", "),
          address2.uprn.getOrElse(4567).toString -> address2.address.mkString(", ")
        ) // TODO this should come from call to GDS lookup.
    }.recoverWith {
      case e: Throwable => Future {
        Logger.error(s"Ordnance Survey postcode lookup service error: ${e}")
        Map.empty[String, String]
      }
    }
  }

  override def lookupAddress(uprn: String): AddressViewModel = {
    val addresses = Map(
      address1.uprn.getOrElse(1234).toString -> address1,
      address2.uprn.getOrElse(4567).toString -> address2
    ) // TODO this should come from call to GDS lookup.

    addresses(uprn)
  }
}
