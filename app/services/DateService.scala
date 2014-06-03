package services

trait DateService {
  def today: models.DayMonthYear
  def dateTimeISOChronology: String
}
