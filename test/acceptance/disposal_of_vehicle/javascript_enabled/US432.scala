package acceptance.disposal_of_vehicle.javascript_enabled

import org.junit.runner.RunWith
import cucumber.api.junit.Cucumber
import cucumber.api.CucumberOptions

@RunWith(classOf[Cucumber])
@CucumberOptions(
  format = Array("pretty", "html:target/cucumber-acceptance-html-report-US432"),
  strict = true,
  features = Array("test/acceptance/disposal_of_vehicle/javascript_enabled/US432.feature"),
  glue = Array("classpath:helpers.hooks", "classpath:helpers.steps_javascript_enabled")
)
class US432 {

}
