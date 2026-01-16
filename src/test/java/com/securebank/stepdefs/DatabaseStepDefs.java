package com.securebank.stepdefs;

import io.cucumber.java.en.*;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import org.testng.Assert;

import com.securebank.pages.DashboardPage;
import com.securebank.utils.DatabaseHelper;
import com.securebank.utils.DriverManager;

import java.sql.*;
import java.util.*;

public class DatabaseStepDefs {
    private DatabaseHelper dbHelper = new DatabaseHelper();
    private ResultSet resultSet;
    private List<String> transactionIds;
    private boolean transactionExists;
    private double queriedAmount;
    private String queriedType;
    private int queriedAccountId;
    private String queriedStatus;
    private String queriedDescription;
    private String queriedTransactionId;
    private double queriedBalanceAfter;

    @Before(value = "@database")
    public void openConnection() throws Exception {
    	System.out.println("Opening database connection...");
        dbHelper.openConnection();
    }

    @When("I query the transactions table for transaction_id {string}")
    public void i_query_transactions_table_for_transaction_id(String txnId) throws Exception {
        resultSet = dbHelper.queryTransactionById(txnId);
        transactionExists = resultSet.next();
        if (transactionExists) {
            queriedAccountId = resultSet.getInt("account_id");
            queriedType = resultSet.getString("transaction_type");
            queriedAmount = resultSet.getDouble("amount");
            queriedStatus = resultSet.getString("status");
        }
    }

    @Then("the transaction should exist with account_id {int}, transaction_type {string}, amount {double}, and status {string}")
    public void the_transaction_should_exist_with_details(int accountId, String type, double amount, String status) {
        Assert.assertTrue(transactionExists, "Transaction does not exist");
        Assert.assertEquals(accountId, queriedAccountId);
        Assert.assertEquals(type, queriedType);
        Assert.assertEquals(amount, queriedAmount, 0.01);
        Assert.assertEquals(status, queriedStatus);
    }

    @When("I query the transactions table for withdrawals for account_id {int}")
    public void i_query_transactions_table_for_withdrawals_for_account_id(int accountId) throws Exception {
        resultSet = dbHelper.queryWithdrawalsByAccountId(accountId);
        transactionExists = resultSet.next();
    }

    @Then("no transactions should be found")
    public void no_transactions_should_be_found() {
        Assert.assertFalse(transactionExists, "Unexpected transactions found");
    }

    @When("I query the transactions table for all transactions for account_id {int}")
    public void i_query_transactions_table_for_all_transactions_for_account_id(int accountId) throws Exception {
        resultSet = dbHelper.queryAllTransactionsByAccountId(accountId);
        if (resultSet.next()) {
            resultSet.getInt(1);
        }
    }

    @When("I query the transactions table for account_id {int} between {string} and {string}")
    public void i_query_transactions_table_for_account_id_between_dates(int accountId, String from, String to) throws Exception {
        transactionIds = dbHelper.queryTransactionIdsByAccountIdAndDate(accountId, from, to);
    }

    @Then("the following transaction_ids should be returned:")
    public void the_following_transaction_ids_should_be_returned(io.cucumber.datatable.DataTable dataTable) {
        List<String> expectedIds = dataTable.asList();
        Assert.assertEquals(expectedIds, transactionIds);
    }

    @When("I query the transactions table for the latest transaction for {string} account and user {string}")
    public void i_query_latest_transaction_for_account_and_user(String fromAccount, String username) throws Exception {
        int userId = dbHelper.getUserIdByUsername(username);
        if (userId == -1) throw new Exception("User not found: " + username);
        int accountId = dbHelper.getAccountIdByUserIdAndType(userId, fromAccount);
        if (accountId == -1) throw new Exception("Account not found for user: " + username + ", type: " + fromAccount);
        resultSet = dbHelper.queryLatestTransactionForAccount(accountId);
        transactionExists = resultSet.next();
        if (transactionExists) {
            queriedAmount = resultSet.getDouble("amount");
            queriedType = resultSet.getString("transaction_type");
            queriedStatus = resultSet.getString("status");
            queriedAccountId = accountId;
            queriedDescription = resultSet.getString("description");
            queriedTransactionId = resultSet.getString("transaction_id");
            queriedBalanceAfter = resultSet.getDouble("balance_after");
        }
    }

    @Then("the transaction should exist in the database with description {string}, amount {string}, from {string}, to {string}, and status {string}")
    public void transaction_should_exist_in_db(String description, String amount, String from, String to, String status) throws Exception {
    	Assert.assertTrue(transactionExists, "Transaction does not exist");
        String expectedDescription = (description == null || description.trim().isEmpty()) ? "Transfer" : description;
        String actualDescription = (queriedDescription == null || queriedDescription.trim().isEmpty()) ? "Transfer" : queriedDescription;
        Assert.assertEquals(actualDescription, expectedDescription);
        double expectedAmount = Math.abs(Double.parseDouble(amount));
        double actualAmount = Math.abs(queriedAmount);
        Assert.assertEquals(actualAmount, expectedAmount, 0.01);
        Assert.assertEquals(status, queriedStatus);
        Assert.assertNotNull(queriedTransactionId, "No transaction_id found");
        Assert.assertTrue(queriedTransactionId.contains("TXN"), "transaction_id does not contain 'TXN': " + queriedTransactionId);
    }

    @Then("the {string} account balance in the dashboard should match the database value")
    public void account_balance_ui_should_match_db(String accountType) {
        DashboardPage dashboardPage = new DashboardPage(DriverManager.getDriver());
        double dashboardBalance;
        if (accountType.equalsIgnoreCase("checking")) {
            dashboardBalance = dashboardPage.getCheckingBalance();
        } else if (accountType.equalsIgnoreCase("savings")) {
            dashboardBalance = dashboardPage.getSavingsBalance();
        } else {
            throw new IllegalArgumentException("Unknown account type: " + accountType);
        }
        
        Assert.assertEquals(dashboardBalance, queriedBalanceAfter, 0.01,
            "UI balance and DB balance do not match for " + accountType + " account");
    }

    @After(value = "@database")
    public void closeConnection() throws Exception {
        if (resultSet != null) resultSet.close();
        System.out.println("Closing database connection...");
        dbHelper.closeConnection();
    }
}
