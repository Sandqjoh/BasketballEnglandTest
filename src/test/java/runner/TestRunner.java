package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "src/test/resources/features",  // Korrekt sökväg till dina feature-filer
        glue = "stepdefinitions",  // Korrekt paket där dina StepDefinitions finns
        plugin = {"pretty", "html:target/cucumber-reports"},  // Rapportering och loggning
        monochrome = true  // Gör utdata mer läsbar i terminalen
)
public class TestRunner {
    // Denna klass kör alla tester som definieras i feature-filerna
}
