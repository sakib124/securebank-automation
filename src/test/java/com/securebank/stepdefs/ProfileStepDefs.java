package com.securebank.stepdefs;

import io.cucumber.java.en.*;
import com.securebank.utils.DriverManager;
import com.securebank.pages.ProfilePage;
import org.testng.Assert;

public class ProfileStepDefs {
	private ProfilePage profilePage = new ProfilePage(DriverManager.getDriver());

	@Given("I navigate to the profile page")
	public void i_navigate_to_profile_page() {
		profilePage.navigateToProfilePage();
	}

	@Then("I should see first name {string}")
	public void i_should_see_first_name(String expected) {
		Assert.assertEquals(profilePage.getFirstName(), expected);
	}

	@Then("I should see last name {string}")
	public void i_should_see_last_name(String expected) {
		Assert.assertEquals(profilePage.getLastName(), expected);
	}

	@Then("I should see email {string}")
	public void i_should_see_email(String expected) {
		Assert.assertEquals(profilePage.getEmail(), expected);
	}

	@Then("I should see phone {string}")
	public void i_should_see_phone(String expected) {
		Assert.assertEquals(profilePage.getPhone(), expected);
	}

	@Then("I should see address {string}")
	public void i_should_see_address(String expected) {
		Assert.assertEquals(profilePage.getAddress(), expected);
	}
}