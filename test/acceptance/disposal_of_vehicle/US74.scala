package acceptance.disposal_of_vehicle

import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith
import cucumber.api.CucumberOptions

@RunWith(classOf[Cucumber])
@CucumberOptions(
  format = Array("pretty", "html:target/cucumber-acceptance-html-report-US74"),
  strict = true,
  features = Array("test/acceptance/disposal_of_vehicle/features/US74.feature"),
  glue = Array("classpath:helpers.hooks", "classpath:helpers.steps")
)
class US74 {
}