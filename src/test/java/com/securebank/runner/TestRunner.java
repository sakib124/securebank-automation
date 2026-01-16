package com.securebank.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/features",
    glue = {"com.securebank.stepdefs", "com.securebank.hooks"},
    plugin = {"pretty", "html:target/cucumber-report.html"},
    monochrome = true,
    tags = "@login"
)
public class TestRunner extends AbstractTestNGCucumberTests {
}
