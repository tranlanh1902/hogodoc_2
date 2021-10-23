package elpisclothing.testRunner;

import org.junit.runner.RunWith;
import io.cucumber.junit.CucumberOptions;
import net.serenitybdd.cucumber.CucumberWithSerenity;

@CucumberOptions(features = "src/test/resources", 
glue = "elpisclothing.StepsDefinitions", 
tags = "@login")

@RunWith(CucumberWithSerenity.class)
public class elpisclothingTestRunner {
}

