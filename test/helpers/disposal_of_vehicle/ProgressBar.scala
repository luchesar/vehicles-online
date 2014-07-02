package helpers.disposal_of_vehicle

import play.api.test.FakeApplication
import helpers.webbrowser.TestGlobal

object ProgressBar {
  val fakeApplicationWithProgressBarFalse = FakeApplication(
    withGlobal = Some(TestGlobal),
    additionalConfiguration = Map("progressBar.enabled" -> "false"))

  val fakeApplicationWithProgressBarTrue = FakeApplication(
    withGlobal = Some(TestGlobal),
    additionalConfiguration = Map("progressBar.enabled" -> "true"))
}
