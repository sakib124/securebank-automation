package com.securebank.stepdefs;

import com.securebank.pages.LoginPage;
import com.securebank.pages.DashboardPage;
import com.securebank.utils.DriverManager;
import io.cucumber.java.en.*;
import org.testng.Assert;

public class LoginStepDefs {
    private LoginPage loginPage = new LoginPage(DriverManager.getDriver());
    private DashboardPage dashboardPage = new DashboardPage(DriverManager.getDriver());

    @Given("I am on the login page")
    public void i_am_on_the_login_page() {
        loginPage.driver.get("http://localhost:8080/index.html");
    }

    @When("I login with username {string} and password {string}")
    public void i_login_with_username_and_password(String username, String password) {
        loginPage.login(username, password);
    }

    @Then("I should see the dashboard with welcome message {string}")
    public void i_should_see_dashboard_with_welcome_message(String expectedMessage) {
        Assert.assertTrue(dashboardPage.getWelcomeMessage().contains(expectedMessage));
    }

    @Then("the {string} field should be required and login should not proceed")
    public void field_should_be_required_and_login_should_not_proceed(String fieldName) {
        Assert.assertEquals(loginPage.waitForVisibility(loginPage.getLocatorForField(fieldName)).getAttribute("required"), "true");
    }

    @Then("I should see error message {string}")
    public void i_should_see_error_message(String expectedError) {
        Assert.assertEquals(loginPage.getErrorMessage(), expectedError);
    }

    @When("I logout")
    public void i_logout() {
        dashboardPage.clickLogout();
    }
}