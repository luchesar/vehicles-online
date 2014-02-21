package object app {
  import play.api.Play
  import scala.util.{Success, Try}

  val mb = 131072

  def convertToMB(bytes: Long) = {
    bytes / mb
  }

  def convertToBytes(megaBytes: Long) = {
    megaBytes * mb
  }

  object ConfigProperties {
    def getProperty(property: String, default: Int) = Try(Play.current.configuration.getInt(property).getOrElse(default)) match {
      case Success(s) => s
      case _ => default
    }

    def getProperty(property: String, default: String) = Try(Play.current.configuration.getString(property).getOrElse(default)) match {
      case Success(s) => s
      case _ => default
    }

    def getProperty(property: String, default: Boolean) = Try(Play.current.configuration.getBoolean(property).getOrElse(default)) match {
      case Success(s) => s
      case _ => default
    }

    def getProperty(property: String, default: Long) = Try(Play.current.configuration.getLong(property).getOrElse(default)) match {
      case Success(s) => s
      case _ => default
    }
  }

  object ChangeOfAddress {

    // TODO make sure all html pages, controllers, formSpec & controllerSpec, integrationSpec use the IDs from this package
    // Page 6 IDA login page
    object LoginPage {
      val usernameId = "username"
      val passwordId = "password"
    }

    // Page 8
    object Authentication {
      val pinFormID = "PIN"
    }

    // Page 9
    object V5cSearch {
      val v5cReferenceNumberID = "V5cReferenceNumber"
      val v5cRegistrationNumberID = "V5CRegistrationNumber"
      val v5cPostcodeID = "V5cPostcode"
    }

  }

  object DisposalOfVehicle {

    // page 2 - setup-trade-details
    object SetupTradeDetails {
      val traderBusinessNameID = "traderBusinessName"
      val traderPostcodeID = "traderPostcode"
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

    object DisposeConfirmation {
      val emailAddressId = "emailAddress"
    }

  }

}