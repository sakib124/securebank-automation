package com.securebank.stepdefs;

import io.cucumber.java.en.*;

import org.openqa.selenium.By;
import org.testng.Assert;

import com.securebank.utils.DriverManager;
import com.securebank.pages.DashboardPage;
import com.securebank.pages.TransferPage;

public class TransferStepDefs {
	private DashboardPage dashboardPage = new DashboardPage(DriverManager.getDriver());
	private TransferPage transferPage = new TransferPage(DriverManager.getDriver());
	private double initialCheckingBalance;
	private double initialSavingsBalance;

	@Given("I navigate to the transfer page")
	public void i_navigate_to_transfer_page() {
		// Before navigating, capture initial balances from dashboard
		initialCheckingBalance = dashboardPage.getCheckingBalance();
		initialSavingsBalance = dashboardPage.getSavingsBalance();
		transferPage.navigateToTransferPage();
	}

	@When("I select {string} as the from account")
	public void i_select_from_account(String account) {
		transferPage.selectFromAccount(account);
	}

	@When("I select {string} as the to account")
	public void i_select_to_account(String account) {
		transferPage.selectToAccount(account);
	}

	@When("I enter amount {string}")
	public void i_enter_amount(String amount) {
		Double.parseDouble(amount);
		transferPage.enterAmount(amount);
	}

	@When("I enter description {string}")
	public void i_enter_description(String description) {
		transferPage.enterDescription(description);
	}

	@When("I submit the transfer")
	public void i_submit_transfer() {
		transferPage.submitForm();
	}

	@When("I confirm the transfer")
	public void i_confirm_transfer() {
		transferPage.confirmTransfer();
	}

	@Then("I should see success message {string}")
	public void i_should_see_success_message(String message) {
		String actualMessage = transferPage.getSuccessMessage();
		Assert.assertEquals(actualMessage, message);
	}

	@Then("I should see a confirmation modal")
	public void i_should_see_confirmation_modal() {
		transferPage.getConfirmationModal();
	}

	@Then("the {string} balance should increase by {string}")
	public void balance_should_increase_by(String account, String amount) {
		transferPage.click(By.id("nav-dashboard"));
		transferPage.wait.until(driver -> driver.getCurrentUrl().contains("dashboard.html"));
		double amt = Double.parseDouble(amount);
		Assert.assertTrue(
				dashboardPage.isBalanceIncreasedBy(account,
						account.equalsIgnoreCase("checking") ? initialCheckingBalance : initialSavingsBalance, amt),
				account + " balance did not increase correctly");
	}

	@Then("the {string} balance should decrease by {string}")
	public void balance_should_decrease_by(String account, String amount) {
		double amt = Double.parseDouble(amount);
		Assert.assertTrue(
				dashboardPage.isBalanceDecreasedBy(account,
						account.equalsIgnoreCase("checking") ? initialCheckingBalance : initialSavingsBalance, amt),
				account + " balance did not decrease correctly");
	}

	@Then("I should see a recent transaction with description {string}, amount {string}, and status {string}")
	public void i_should_see_recent_transaction(String description, String amount, String status) {
		// If description is blank, use default
		if (description == null || description.trim().isEmpty()) {
			description = "Transfer";
		}
		Assert.assertTrue(dashboardPage.hasRecentTransaction(description, amount, status),
				String.format("Recent transaction not found: %s, %s, %s", description, amount, status));
	}

	@When("I leave the {string} field empty")
	public void leave_field_empty(String field) {
		transferPage.clearField(field);
	}

	@When("I click the clear button")
	public void i_click_the_clear_button() {
		transferPage.clickClear();
	}

	@When("I enter zero or a negative value in the {string} field")
	public void enter_zero_or_negative(String field) {
		if (field.equalsIgnoreCase("amount")) {
			transferPage.enterZeroOrNegativeAmount();
		}
	}

	@When("I enter an amount greater than my available balance")
	public void enter_amount_exceeds_balance() {
		transferPage.enterExcessiveAmount(initialCheckingBalance, initialSavingsBalance);
	}

	@Then("I should see a missing field prompt {string}")
	public void should_see_a_missing_field_prompt(String expected) {
		String[] fieldIds = { "from-account", "to-account", "amount", "description" };
		String actualMessage = transferPage.getValidationMessageForFields(fieldIds);
		Assert.assertTrue(actualMessage != null && actualMessage.contains(expected),
				"Expected JS required field error to contain: '" + expected + "', but got: '" + actualMessage + "'");
	}

	@Then("all fields in the transfer form should be reset to their default values")
	public void all_fields_should_be_reset() {

		Assert.assertEquals("Select Account", transferPage.isFromAccountEmpty(), "From Account not reset");
		Assert.assertEquals("Select Account", transferPage.isToAccountEmpty(), "To Account not reset");
		Assert.assertTrue(transferPage.isAmountEmpty(), "Amount not reset");
		Assert.assertTrue(transferPage.isDescriptionEmpty(), "Description not reset");
	}
}