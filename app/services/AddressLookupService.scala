package services

trait AddressLookupService {
  def invoke(postcode: String): Map[String, String]
}