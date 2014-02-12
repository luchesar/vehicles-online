package models.domain.common

case class Address(line1: String, line2: Option[String] = None, line3: Option[String] = None, line4: Option[String] = None, postCode: String) {
  def toViewFormat() = {
    Seq(Some(line1), line2, line3, line4, Some(postCode)).flatten.mkString(", ")
    //s"${line1}, ${line2.getOrElse("")}, ${line3.getOrElse("")}, ${line4.getOrElse("")}, $postCode"
  }
}