import models.domain.change_of_address._
import models.domain.change_of_address.LoginConfirmationModel
import models.domain.change_of_address.V5cSearchConfirmationModel
import models.domain.change_of_address.V5cSearchResponse
import models.domain.common.Address
import play.api.libs.json.Json
import play.api.Play

import scala.util.{Success, Try}

package object app {

  object JsonSupport {
    implicit val writeV5cSearch = Json.writes[V5cSearchModel]
    implicit val v5cSearchConfirmationModel = Json.reads[V5cSearchConfirmationModel]
    implicit val v5cSearchResponse = Json.reads[V5cSearchResponse]

    implicit val writeLoginPage = Json.writes[LoginPageModel]
    implicit val address = Json.reads[Address]
    implicit val loginConfirmationModel = Json.reads[LoginConfirmationModel]
    implicit val loginResponse = Json.reads[LoginResponse]
  }

  val mb = 131072
  def convertToMB(bytes:Long) = {
    bytes / mb
  }

  def convertToBytes(megaBytes:Long) = {
    megaBytes * mb
  }

  object ConfigProperties  {
    def getProperty(property:String,default:Int) = Try(Play.current.configuration.getInt(property).getOrElse(default)) match { case Success(s) => s case _ => default}
    def getProperty(property:String,default:String) = Try(Play.current.configuration.getString(property).getOrElse(default)) match { case Success(s) => s case _ => default}
    def getProperty(property:String,default:Boolean) = Try(Play.current.configuration.getBoolean(property).getOrElse(default)) match { case Success(s) => s case _ => default}
    def getProperty(property:String,default:Long) = Try(Play.current.configuration.getLong(property).getOrElse(default)) match { case Success(s) => s case _ => default}
  }



    // TODO make sure all html pages, controllers, formSpec & controllerSpec, integrationSpec use the IDs from this package

  object DisposalOfVehicle {
    // page 2 - setup-trade-details
    object SetupTradeDetails {

    }

    // page 3 - business choose your address
    object BusinessAddressSelect {
      val businessNameId = "disposal_businessChooseYourAddress_businessName"
      val addressSelectId = "disposal_businessChooseYourAddress_addressSelect"
      val FirstAddress = "1"
      val SecondAddress = "2"
    }

    // vehicle-lookup
    object VehicleLookup {
      val v5cReferenceNumberID = "v5cReferenceNumber"
      val v5cRegistrationNumberID = "v5cRegistrationNumber"
      val v5cKeeperNameID = "v5cKeeperName"
      val v5cPostcodeID = "v5cPostcode"
    }

    object Dispose {
      val consentId = "consent"
      val mileageId = "mileage"
      val dateOfDisposalId = "dateOfDisposal"
    }
  }
}