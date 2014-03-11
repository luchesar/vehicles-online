package pages.disposal_of_vehicle

import helpers.webbrowser.{WebBrowserDSL, Page}
import org.openqa.selenium.WebDriver
import models.domain.disposal_of_vehicle.{AddressViewModel, DealerDetailsModel}

object CacheSetup extends Page with WebBrowserDSL {
  override val url: String = ""
  override val title: String = ""

  def setupTraderDetails(implicit driver: WebDriver) = {
    val key = mappings.disposal_of_vehicle.DealerDetails.dealerDetailsCacheKey
    val address = AddressViewModel(address= Seq("44 Hythe Road", "White City", "London", "NW10 6RJ"))
    val value = DealerDetailsModel(dealerName = "", dealerAddress = address)


    //play.api.cache.Cache.set(key, value)

  }
}
