package net.futureset.test.acceptance;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import io.cucumber.spring.CucumberContextConfiguration;
import net.futureset.app.HelloWorld;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;

@CucumberOptions(
        snippets = CucumberOptions.SnippetType.CAMELCASE,
        glue = {"net.futureset.test.acceptance"},
        plugin = {
            "html:build/reports/cucumber/index.html",
            "json:build/reports/cucumber/cucumber.json",
            "pretty",
            "junit:build/reports/cucumber/cucumber-TEST.xml"
        },
        features = "classpath:features")
@SpringBootTest(classes = HelloWorld.class, useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@CucumberContextConfiguration
@RunWith(Cucumber.class)
public class BddTest {

    @Test
    public void placeholder() {}
}
