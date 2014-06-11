package acceptance.disposal_of_vehicle

import cucumber.api.CucumberOptions
import cucumber.api.junit.Cucumber
import org.junit.runner.RunWith

@RunWith(classOf[Cucumber])
@CucumberOptions(
  format = Array("pretty", "html:target/cucumber-acceptance-html-report-US320"),
  strict = true,
  features = Array("test/acceptance/disposal_of_vehicle/US320.feature"),
  glue = Array("classpath:helpers.hooks", "classpath:helpers.steps")
)
class US320 {

}
