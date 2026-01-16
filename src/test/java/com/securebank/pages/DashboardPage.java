package com.securebank.pages;

import com.securebank.utils.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class DashboardPage extends BasePage {
    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    private By welcomeMessage = By.id("welcome-message");
    private By logoutButton = By.id("logout-button");
    private By checkingBalance = By.id("checking-balance");
    private By creditBalance = By.id("credit-balance");
    private By savingsBalance = By.id("savings-balance");
    private By recentTransactionsRows = By.cssSelector("#transactions-table tbody tr");

    public String getWelcomeMessage() {
        return waitForVisibility(welcomeMessage).getText();
    }

    public void clickLogout() {
        click(logoutButton);
    }

    public double getCheckingBalance() {
        String text = waitForVisibility(checkingBalance).getText();
        return parseBalance(text);
    }

    public double getSavingsBalance() {
        String text = waitForVisibility(savingsBalance).getText();
        return parseBalance(text);
    }

    private double parseBalance(String text) {
        return Double.parseDouble(text.replaceAll("[^0-9.]+", ""));
    }

    public boolean isSavingsBalanceIncreasedBy(double initial, double amount) {
        double newSavings = getSavingsBalance();
        return Math.abs(newSavings - (initial + amount)) < 0.01;
    }

    public boolean isCheckingBalanceDecreasedBy(double initial, double amount) {
        double newChecking = getCheckingBalance();
        return Math.abs(newChecking - (initial - amount)) < 0.01;
    }

    public boolean isCheckingBalanceIncreasedBy(double initial, double amount) {
        double newChecking = getCheckingBalance();
        return Math.abs(newChecking - (initial + amount)) < 0.01;
    }
    public boolean isSavingsBalanceDecreasedBy(double initial, double amount) {
        double newSavings = getSavingsBalance();
        return Math.abs(newSavings - (initial - amount)) < 0.01;
    }

    public boolean isBalanceIncreasedBy(String account, double initial, double amount) {
        if (account.equalsIgnoreCase("checking")) {
            return isCheckingBalanceIncreasedBy(initial, amount);
        } else if (account.equalsIgnoreCase("savings")) {
            return isSavingsBalanceIncreasedBy(initial, amount);
        } else {
            throw new IllegalArgumentException("Unknown account: " + account);
        }
    }
    public boolean isBalanceDecreasedBy(String account, double initial, double amount) {
        if (account.equalsIgnoreCase("checking")) {
            return isCheckingBalanceDecreasedBy(initial, amount);
        } else if (account.equalsIgnoreCase("savings")) {
            return isSavingsBalanceDecreasedBy(initial, amount);
        } else {
            throw new IllegalArgumentException("Unknown account: " + account);
        }
    }

    public boolean hasRecentTransaction(String description, String amount, String status) {
        java.util.List<WebElement> rows = wait.until(
            org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfAllElementsLocatedBy(recentTransactionsRows));
        if (rows.isEmpty()) {
            return false;
        }
        WebElement row = rows.get(0);
        java.util.List<WebElement> cells = row.findElements(By.tagName("td"));
        if (cells.size() < 4) {
            return false;
        }
        String rowDescription = cells.get(1).getText().trim();
        String rowAmount = cells.get(2).getText().trim();
        String rowStatus = cells.get(3).getText().trim();
        return rowDescription.equals(description) && rowAmount.equals(amount) && rowStatus.equals(status);
    }

    public void login(String username, String password) {
        driver.get(Config.BASE_URL);
        sendKeys(By.id("username"), username);
        sendKeys(By.id("password"), password);
        click(By.id("login-button"));
        wait.until(driver -> driver.getCurrentUrl().contains("dashboard.html"));
    }

    public void navigateToDashboard() {
        click(By.id("nav-dashboard"));
        wait.until(driver -> driver.getCurrentUrl().contains("dashboard.html"));
    }

    public boolean isCheckingBalanceVisible() {
        try {
            return waitForVisibility(checkingBalance).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isSavingsBalanceVisible() {
        try {
            return waitForVisibility(savingsBalance).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isCreditBalanceVisible() {
        try {
            return waitForVisibility(creditBalance).isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}