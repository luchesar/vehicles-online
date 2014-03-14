package services.address_lookup.gds

import services.AddressLookupService
import models.domain.disposal_of_vehicle.AddressViewModel
import utils.helpers.Config
import play.api.Logger
import scala.concurrent.{Future, ExecutionContext}
import ExecutionContext.Implicits.global
import javax.inject.Inject
import play.api.libs.ws.Response
import services.address_lookup.gds.domain.Address
import services.address_lookup.gds.domain.JsonFormats.addressFormat

class AddressLookupServiceImpl @Inject()(ws: services.WebService) extends AddressLookupService {
  val baseUrl = s"${Config.gdsAddressLookupBaseUrl}"

  override protected def callPostcodeWebService(postcode: String): Future[Response] = ???

  override protected def callUprnWebService(uprn: String): Future[Response] = ???

  private def extractFromJson(resp: Response): Seq[Address] = {
    try {
      resp.json.as[Seq[Address]]
    }
    catch {
      case e: Throwable => Seq.empty //  return empty seq given invalid json
    }
  }

  override def fetchAddressesForPostcode(postcode: String): Future[Seq[(String, String)]] = {
    def toUprnsAndAddresses(resp: Response): Seq[(String, String)] = {
      extractFromJson(resp) map {
        address => (address.presentation.uprn, address.presentation.toViewModel.mkString(", "))
      }
    }

    callPostcodeWebService(postcode).map {
      resp =>
        Logger.debug(s"Http response code from GDS postcode lookup service was: ${resp.status}")
        if (resp.status == play.api.http.Status.OK) toUprnsAndAddresses(resp)
        else Seq.empty // The service returned http code other than 200
    }.recover {
      case e: Throwable => Seq.empty
    }
  }

  override def fetchAddressForUprn(uprn: String): Future[Option[AddressViewModel]] = {
    def toAddressViewModel(resp: Response) = {
      val addresses = extractFromJson(resp)
      require(addresses.length >= 1, s"Should be at least one address for the UPRN: $uprn")
      val addressViewModel = AddressViewModel(uprn = Some(addresses.head.presentation.uprn.toLong), address = addresses.head.presentation.toViewModel)
      Some(addressViewModel) // Only return one address per UPRN
    }

    callUprnWebService(uprn).map {
      resp =>
        Logger.debug(s"Http response code from GDS postcode lookup service was: ${resp.status}")
        if (resp.status == play.api.http.Status.OK) toAddressViewModel(resp)
        else None
    }.recover {
      case e: Throwable => {
        Logger.error(s"GDS uprn lookup service error: ${e.getStackTraceString}")
        None
      }
    }
  }
}
