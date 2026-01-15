package com.securebank.stepdefs;

import io.cucumber.java.en.*;

import org.openqa.selenium.By;
import org.testng.Assert;

import com.securebank.utils.DriverManager;
import com.securebank.pages.TransactionsPage;

public class TransactionsStepDefs {
    private TransactionsPage transactionsPage = new TransactionsPage(DriverManager.getDriver());

    @Given("I navigate to the transactions page")
    public void i_navigate_to_transactions_page() {
        transactionsPage.navigateToTransactionsPage();
    }

    @When("I select {string} in the account filter")
    public void i_select_account_filter(String account) {
        transactionsPage.selectAccountFilter(account);
    }

    @When("I select {string} in the type filter")
    public void i_select_type_filter(String type) {
        transactionsPage.selectTypeFilter(type);
    }

    @When("I set the date range from {string} to {string}")
    public void i_set_date_range(String from, String to) {
    	transactionsPage.setDateRange(from, to);
    }

    @When("I apply the filters")
    public void i_apply_filters() {
        transactionsPage.applyFilters();
    }

    @Then("I should see a transaction with description {string}, amount {string}, from {string}, to {string}, and status {string} in the transactions history")
    public void i_should_see_transaction_in_history(String description, String amount, String from, String to, String status) {
        if (description == null || description.trim().isEmpty()) {
            description = "Transfer";
        }
        transactionsPage.navigateToTransactionsPage();
        boolean result = transactionsPage.hasTransferRows(description, amount, from, to, status);
        Assert.assertTrue(result, "First two transaction rows do not match expected transfer details for both accounts");
    }

    @When("I select {string} from the Account filter and apply filters")
    public void i_select_from_the_account_filter_and_apply_filters(String account) {
        transactionsPage.selectAccountFilter(account);
        transactionsPage.applyFilters();
    }

    @Then("only transactions for the {string} account should be displayed")
    public void only_transactions_for_the_account_should_be_displayed(String account) {
        Assert.assertTrue(transactionsPage.allRowsMatchAccount(account));
    }

    @When("I select {string} from the Type filter and apply filters")
    public void i_select_from_the_type_filter_and_apply_filters(String type) {
        transactionsPage.selectTypeFilter(type);
        transactionsPage.applyFilters();
    }

    @Then("only {string} transactions should be displayed")
    public void only_transactions_should_be_displayed(String type) {
        Assert.assertTrue(transactionsPage.allRowsMatchType(type));
    }

    @When("I select a valid From Date and To Date and apply filters")
    public void i_select_a_valid_from_date_and_to_date_and_apply_filters() {
    	transactionsPage.setDateRangeFields(By.id("filter-date-from"), "2026", "01", "01", By.id("filter-date-to"),
				"2026", "01", "13");

        transactionsPage.applyFilters();
    }

    @Then("only transactions within the selected date range should be displayed")
    public void only_transactions_within_the_selected_date_range_should_be_displayed() {
        Assert.assertTrue(transactionsPage.allRowsWithinDateRange("2026-01-01", "2026-01-13"));
    }

    @When("I select {string} from the Account filter and {string} from the Type filter and apply filters")
    public void i_select_from_the_account_filter_and_from_the_type_filter_and_apply_filters(String account, String type) {
        transactionsPage.selectAccountFilter(account);
        transactionsPage.selectTypeFilter(type);
        transactionsPage.applyFilters();
    }

    @Then("only deposit transactions for the Savings account should be displayed")
    public void only_deposit_transactions_for_the_savings_account_should_be_displayed() {
        Assert.assertTrue(transactionsPage.allRowsMatchAccountAndType("Savings", "Deposit"));
    }

    @When("I select filter criteria that match no transactions and apply filters")
    public void i_select_filter_criteria_that_match_no_transactions_and_apply_filters() {
        transactionsPage.selectAccountFilter("NonExistentAccount");
        transactionsPage.applyFilters();
    }

    @Then("the transactions table should display no results")
    public void the_transactions_table_should_display_no_results_or_a_message() {
        Assert.assertEquals("Showing 0 of 0 transactions", transactionsPage.checkForNoResults());
    }

    @And("I have applied some filters")
    public void i_have_applied_some_filters() {
        transactionsPage.selectAccountFilter("Checking");
        transactionsPage.applyFilters();
    }

    @When("I click the {string} button")
    public void i_click_the_button(String button) {
        if (button.equalsIgnoreCase("Reset")) {
            transactionsPage.resetFilters();
        }
    }

    @Then("all filter fields should be reset to their default values")
    public void all_filter_fields_should_be_reset_to_their_default_values() {
        Assert.assertTrue(transactionsPage.filtersAreDefault());
    }

    @And("there are more transactions than fit on one page")
    public void there_are_more_transactions_than_fit_on_one_page() {
        Assert.assertTrue(transactionsPage.hasPagination());
    }

    @When("I click the Next page button")
    public void i_click_the_next_page_button() {
        transactionsPage.clickNextPage();
    }

    @Then("the next set of transactions should be displayed")
    public void the_next_set_of_transactions_should_be_displayed() {
        Assert.assertTrue(transactionsPage.isNextPageDisplayed());
    }

    @When("I click the Previous page button")
    public void i_click_the_previous_page_button() {
        transactionsPage.clickPrevPage();
    }

    @Then("the previous set of transactions should be displayed")
    public void the_previous_set_of_transactions_should_be_displayed() {
        Assert.assertTrue(transactionsPage.isPreviousPageDisplayed());
    }

    @Then("the Previous and Next page buttons should be disabled")
    public void the_previous_and_next_page_buttons_should_be_disabled() {
        Assert.assertFalse(transactionsPage.isNextPageEnabled());
        Assert.assertFalse(transactionsPage.isPrevPageEnabled());
    }
}
