package models.domain.disposal_of_vehicle

case class AddressLinesModel(line1: Option[String], line2: Option[String] = None, line3: Option[String] = None, line4: Option[String] = None){
  def toViewFormat: Seq[String] = Seq(line1, line2, line3, line4).flatten
}