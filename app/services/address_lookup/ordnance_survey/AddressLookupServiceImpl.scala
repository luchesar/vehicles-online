package services.address_lookup.ordnance_survey

import models.domain.disposal_of_vehicle.AddressViewModel
import utils.helpers.Config
import play.api.Logger
import com.ning.http.client.Realm.AuthScheme
import services.address_lookup.ordnance_survey.domain.{OSAddressbaseDPA, OSAddressbaseResult, OSAddressbaseSearchResponse}
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global
import javax.inject.Inject
import play.api.libs.ws.Response
import services.address_lookup.AddressLookupService

class AddressLookupServiceImpl @Inject()(ws: services.WebService) extends AddressLookupService {
  private def extractFromJson(resp: Response): Option[Seq[OSAddressbaseResult]] = {
    val response = resp.json.asOpt[OSAddressbaseSearchResponse]
    response.flatMap(_.results)
  }

  override def fetchAddressesForPostcode(postcode: String): Future[Seq[(String, String)]] = {
    def sort(addresses: Seq[OSAddressbaseDPA]) = {
      addresses.sortBy(addressDpa => {
        val buildingNumber = addressDpa.buildingNumber.getOrElse("0")
        val buildingNumberSanitised = buildingNumber.replaceAll("[^0-9]", "") // Sanitise building number as it could contain letters which would cause toInt to throw e.g. 107a.
        (buildingNumberSanitised, addressDpa.buildingName) // TODO check with BAs how they would want to sort the list
      })
    }

    def toDropDown(resp: Response): Seq[(String, String)] =
      extractFromJson(resp) match {
        case Some(results) =>
          val addresses = results.flatMap { _.DPA }
          sort(addresses) map { address => (address.UPRN, address.address) } // Sort before translating to drop down format.
        case None =>
          // Handle no results
          Logger.debug(s"No results returned for postcode: $postcode")
          Seq.empty
      }

    ws.callPostcodeWebService(postcode).map { resp =>
      Logger.debug(s"Http response code from Ordnance Survey postcode lookup service was: ${ resp.status }")
      if (resp.status == play.api.http.Status.OK) toDropDown(resp)
      else Seq.empty // The service returned http code other than 200
    }.recover {
      case e: Throwable =>
        Logger.error(s"Ordnance Survey postcode lookup service error: $e")
        Seq.empty
    }
  }

  override def fetchAddressForUprn(uprn: String): Future[Option[AddressViewModel]] = {
    // Extract result from response and return as a view model.
    def toViewModel(resp: Response) =
      extractFromJson(resp) match {
        case Some(results) =>
          val addresses = results.flatMap { _.DPA }
          require(addresses.length >= 1, s"Should be at least one address for the UPRN: $uprn")
          Some(AddressViewModel(uprn = Some(addresses.head.UPRN.toLong), address = addresses.head.address.split(", "))) // Translate to view model.
        case None =>
          Logger.error(s"No results returned by web service for submitted UPRN: $uprn")
          None
      }

    ws.callUprnWebService(uprn).map {
      resp =>
        Logger.debug(s"Http response code from Ordnance Survey uprn lookup service was: ${ resp.status }")
        if (resp.status == play.api.http.Status.OK) toViewModel(resp)
        else None
    }.recover {
      case e: Throwable =>
        Logger.error(s"Ordnance Survey uprn lookup service error: $e")
        None
    }
  }
}
