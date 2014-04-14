package acceptance.disposal_of_vehicle

import org.scalatest.{BeforeAndAfterAll, Matchers, GivenWhenThen, FeatureSpec}
import helpers.webbrowser._
import pages.disposal_of_vehicle.DisposePage
import pages.disposal_of_vehicle.DisposePage._
import services.fakes.FakeDateServiceImpl._
import controllers.disposal_of_vehicle.Helpers.fetchDisposeTransactionTimestampInCache

class US148_disposal_to_trade_date_time_stamp_of_transaction extends FeatureSpec with GivenWhenThen with Matchers with BeforeAndAfterAll with TestHarness {
  private def cacheSetup() = {
    import helpers.disposal_of_vehicle.CacheSetup
    CacheSetup.setupTradeDetails().
      businessChooseYourAddress().
      vehicleDetailsModel().
      vehicleLookupFormModel()
  }

  feature("US148: Disposal to Trade: Date/time stamp of transaction") {
    info("As a DVLA Caseworker")
    info("I want the date and time of the Disposal to Trade transaction to be generated")
    info("So that it can be saved into the Source Record")
    info("")

    scenario("Dispose micro-service is unavailable") {
      new WebBrowser {
        Given("that the motor trader has entered the required information into the Disposal to Trade service")
        cacheSetup()
        go to DisposePage
        dateOfDisposalDay select dateOfDisposalDayValid
        dateOfDisposalMonth select dateOfDisposalMonthValid
        dateOfDisposalYear select dateOfDisposalYearValid
        click on consent
        click on lossOfRegistrationConsent

        When("the motor trader elects to action the disposal submission")
        click on dispose

        Then("a timestamp representing the current date and time is generated and retained")
        fetchDisposeTransactionTimestampInCache match {
          case Some(transactionTimestamp) => transactionTimestamp should include(s"$dateOfDisposalYearValid-$dateOfDisposalMonthValid-$dateOfDisposalDayValid")
          case _ => fail("Should have found transaction timestamp in cache")
        }
      }
    }
  }
}
