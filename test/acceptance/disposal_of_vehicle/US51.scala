package acceptance.disposal_of_vehicle

import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith
import cucumber.api.CucumberOptions

@RunWith(classOf[Cucumber])
@CucumberOptions(
  format = Array("pretty", "html:target/cucumber-acceptance-html-report-US51"),
  strict = true,
  features = Array("test/acceptance/disposal_of_vehicle/features/US51.feature"),
  glue = Array("classpath:helpers.hooks", "classpath:acceptance.disposal_of_vehicle.steps")
)
class US51 {
}