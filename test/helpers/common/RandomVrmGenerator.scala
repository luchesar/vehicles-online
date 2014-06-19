package helpers.common

object RandomVrmGenerator {
  def vrm = {
    // Create random reg in this format: YL07YBX
    s"$randomChar$randomChar$randomNumber$randomNumber$randomChar$randomChar$randomChar"
  }

  private lazy val letters = 'A' to 'Z'

  private def randomChar: String = {
    val randomIndex = util.Random.nextInt(letters.length)
    val randomLetter = letters(randomIndex)
    randomLetter.toString
  }

  private def randomNumber: String = {
    val randomNumber = util.Random.nextInt(10)
    randomNumber.toString
  }

}