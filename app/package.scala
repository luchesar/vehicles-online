import com.google.inject.Guice
import models.domain.change_of_address._
import models.domain.change_of_address.LoginConfirmationModel
import models.domain.change_of_address.V5cSearchConfirmationModel
import models.domain.change_of_address.V5cSearchResponse
import models.domain.common.Address
import modules.DevModule
import play.api.libs.json.Json
import play.api.Play

import scala.util.Success
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

  object AccountStatus {
    val BankBuildingAccount = 'bankBuildingAccount
    val AppliedForAccount = 'appliedForAccount
    val NotOpenAccount = 'notOpenAccount
  }

  object ActingType {
    val Guardian = 'guardian
    val Attorney = 'attorney
    val Appointee = 'appointee
    val Judicial = 'judicial
    val Deputy = 'deputy
    val Curator = 'curator
  }

  object Whereabouts {
    val Home = "Home"
    val Hospital = "Hospital"
    val Holiday = "Holiday"
    val RespiteCare = "Respite Care"
    val CareHome = "Care Home"
    val NursingHome = "Nursing Home"
    val Other = "Other"
  }

  object PaymentFrequency {
    val EveryWeek = 'everyWeek
    val FourWeekly = 'fourWeekly
  }

  object PensionPaymentFrequency {
    val Weekly = "02"
    val Fortnightly = "03"
    val FourWeekly = "04"
    val Monthly = "05"
    val Other = "other" // TODO [SKW] the xsd is inconsistent and needs changing as there is no value for other, so I just made up a value and Jorge will change the schema and can replace this with a sensible value.
/*
    def mapToHumanReadableString(code: models.PensionPaymentFrequency): String = {
      mapToHumanReadableString(code.frequency)
    }
*/
    def mapToHumanReadableString(code: String): String = {
      code match {
        case Weekly => "Weekly"
        case Fortnightly => "Fortnightly"
        case FourWeekly => "Four-weekly"
        case Monthly => "Monthly"
        case Other => "Other"
        case _ => ""
      }
    }
  }

  object StatutoryPaymentFrequency {
    val Weekly = "weekly"
    val Fortnightly = "fortnightly"
    val FourWeekly = "fourWeekly"
    val Monthly = "monthly"
    val Other = "other"

    def mapToHumanReadableString(frequencyCode: String, otherCode: Option[String]): String = frequencyCode match {
      case Weekly => "Weekly"
      case Fortnightly => "Fortnightly"
      case FourWeekly => "Four-weekly"
      case Monthly => "Monthly"
      case Other => otherCode match {
        case Some(s) => "Other: " + s
        case _ => "Other"
      } //+ paymentFrequency.other.getOrElse("")
      case _ => ""
    }
/*
    def mapToHumanReadableString(paymentFrequencyOption: Option[models.PaymentFrequency]): String = paymentFrequencyOption match {
      case Some(s) => mapToHumanReadableString(s.frequency,None)
      case _ => ""
    }

    def mapToHumanReadableStringWithOther(paymentFrequencyOption: Option[models.PaymentFrequency]): String = paymentFrequencyOption match {
      case Some(s) => mapToHumanReadableString(s.frequency,s.other)
      case _ => ""
    }*/
  }

  object XMLValues {
    val NotAsked = "Not asked"
    val NotKnown = "Not known"
    val Yes = "Yes"
    val No = "No"
    val yes = "yes"
    val no = "no"
    val GBP = "GBP"
    val Other = "Other"
  }

  object WhoseNameAccount {
    val YourName = 'yourName
    val Yourpartner = 'partner
    val Both = 'bothNames
    val PersonActingBehalf = 'onBehalfOfYou
    val YouPersonBehalf = 'allNames
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

  object ChangeOfAddress {


    val pinFormID = "PIN"
    val v5cReferenceNumberID = "V5cReferenceNumber"
    val v5cRegistrationNumberID = "V5CRegistrationNumber"
    val v5cPostcodeID = "V5cPostcode"
    val usernameId = "username"
    val passwordId = "password"
    val postcodeId = "Postcode"
  }

  object DisposalOfVehicle {
    val v5cReferenceNumberID = "v5cReferenceNumber"
    val v5cRegistrationNumberID = "v5cRegistrationNumber"
    val v5cKeeperNameID = "v5cKeeperName"
    val v5cPostcodeID = "v5cPostcode"

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
  }
}