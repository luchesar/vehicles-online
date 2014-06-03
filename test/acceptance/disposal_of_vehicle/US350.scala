package acceptance.disposal_of_vehicle

import org.junit.runner.RunWith
import cucumber.api.junit.Cucumber
import cucumber.api.CucumberOptions

@RunWith(classOf[Cucumber])
@CucumberOptions(
  format = Array("pretty", "html:target/cucumber-acceptance-html-report-US350"),
  strict = true,
  features = Array("test/acceptance/disposal_of_vehicle/US350.feature"),
  glue = Array("classpath:helpers.hooks", "classpath:helpers.steps")
)
class US350 {

}
