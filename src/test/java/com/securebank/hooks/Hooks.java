package com.securebank.hooks;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import com.securebank.utils.DriverManager;

public class Hooks {
    @Before
    public void setUp() {
        DriverManager.initDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            byte[] screenshot = DriverManager.getScreenshot();
            if (screenshot != null) {
                scenario.attach(screenshot, "image/png", "Failed Scenario Screenshot");
            }
        }
        DriverManager.quitDriver();
    }
}