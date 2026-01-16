package com.securebank.stepdefs;

import io.cucumber.java.en.*;
import org.testng.Assert;
import com.securebank.pages.DashboardPage;
import com.securebank.utils.DriverManager;
// import com.securebank.utils.DatabaseHelper; // For future DB steps

public class DashboardStepDefs {
    private DashboardPage dashboardPage = new DashboardPage(DriverManager.getDriver());

    @Given("I am logged in as {string} with password {string}")
    public void i_am_logged_in(String username, String password) {
        // Uses DashboardPage.login, which uses BasePage methods
        dashboardPage.login(username, password);
    }

    @When("I navigate to the dashboard")
    public void i_navigate_to_dashboard() {
        dashboardPage.navigateToDashboard();
    }

    @Then("I should see welcome message {string}")
    public void i_should_see_welcome_message(String message) {
        Assert.assertTrue(dashboardPage.getWelcomeMessage().contains(message));
    }

    @Then("I should see checking, savings, and credit balances")
    public void i_should_see_balances() {
        Assert.assertTrue(dashboardPage.isCheckingBalanceVisible());
        Assert.assertTrue(dashboardPage.isSavingsBalanceVisible());
        Assert.assertTrue(dashboardPage.isCreditBalanceVisible());
    }
}