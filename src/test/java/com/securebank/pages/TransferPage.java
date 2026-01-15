package com.securebank.pages;

import org.openqa.selenium.*;

public class TransferPage extends BasePage {
	public TransferPage(WebDriver driver) {
		super(driver);
	}

	private By fromAccountSelect = By.id("from-account");
	private By toAccountSelect = By.id("to-account");
	private By amountInput = By.id("amount");
	private By descriptionInput = By.id("description");
	private By submitButton = By.id("submit-transfer");
	private By confirmationModal = By.id("confirmation-modal");
	private By confrimButton = By.id("confirm-transfer");
	private By clearButton = By.id("reset-form");
	private By errorMessage = By.cssSelector(".alert-danger, .error-message");
	private By successMessage = By.id("success-message");

	public void selectFromAccount(String value) {
		getSelect(fromAccountSelect).selectByValue(value);
	}

	public void selectToAccount(String value) {
		getSelect(toAccountSelect).selectByValue(value);
	}

	public void clearFromAccount() {
		getSelect(fromAccountSelect).selectByIndex(0);
	}

	public void clearToAccount() {
		getSelect(toAccountSelect).selectByIndex(0);
	}

	public void enterAmount(String value) {
		sendKeys(amountInput, value);
	}

	public void clearAmount() {
		sendKeys(amountInput, "");
	}

	public void enterDescription(String value) {
		sendKeys(descriptionInput, value);
	}

	public void submitForm() {
		click(submitButton);
	}

	public void clickClear() {
		click(clearButton);
	}

	public String getErrorMessage() {
		try {
			return waitForVisibility(errorMessage).getText();
		} catch (TimeoutException e) {
			return null;
		}
	}

	public String isFromAccountEmpty() {
		return getSelect(fromAccountSelect).getFirstSelectedOption().getText();
	}

	public String isToAccountEmpty() {
		return getSelect(toAccountSelect).getFirstSelectedOption().getText();
	}

	public boolean isAmountEmpty() {
		return waitForVisibility(amountInput).getAttribute("value").isEmpty();
	}

	public boolean isDescriptionEmpty() {
		return waitForVisibility(descriptionInput).getAttribute("value").isEmpty();
	}

	public void clearField(String field) {
		switch (field.toLowerCase()) {
		case "from account":
			clearFromAccount();
			break;
		case "to account":
			clearToAccount();
			break;
		case "amount":
			clearAmount();
			break;
		case "description":
			enterDescription("");
			break;
		}
	}

	public void enterZeroOrNegativeAmount() {
		enterAmount("0");
		submitForm();
		enterAmount("-10");
	}

	public void enterExcessiveAmount(double checking, double savings) {
		double excessive = Math.max(checking, savings) + 1000;
		enterAmount(String.valueOf(excessive));
	}

	public String getValidationMessageForFields(String[] fieldIds) {
		for (String fieldId : fieldIds) {
			WebElement element;
			try {
				element = driver.findElement(By.id(fieldId));
			} catch (NoSuchElementException e) {
				continue;
			}
			String validationMessage = (String) ((JavascriptExecutor) driver)
					.executeScript("return arguments[0].validationMessage;", element);
			if (validationMessage != null && !validationMessage.isEmpty()) {
				return validationMessage;
			}
		}
		return null;
	}

	// Utility for date fields if needed in future
	public void setDateRangeFields(By fromLocator, String fromYear, String fromMonth, String fromDay, By toLocator,
			String toYear, String toMonth, String toDay) {
		sendKeys(fromLocator, fromYear);
		sendKeys(fromLocator, Keys.TAB);
		sendKeys(fromLocator, fromMonth);
		sendKeys(fromLocator, fromDay);
		sendKeys(toLocator, toYear);
		sendKeys(toLocator, Keys.TAB);
		sendKeys(toLocator, toMonth);
		sendKeys(toLocator, toDay);
	}

	public String getSuccessMessage() {
		try {
			return waitForVisibility(successMessage).getText();
		} catch (TimeoutException e) {
			return null;
		}
	}

	public void navigateToTransferPage() {
		click(By.id("nav-transfer"));
		wait.until(driver -> driver.getCurrentUrl().contains("transfer.html"));
	}

	public void confirmTransfer() {
		click(confrimButton);
		
	}

	public String getConfirmationModal() {
		try {
			return waitForVisibility(confirmationModal).getText();
		} catch (TimeoutException e) {
			return null;
		}
		
	}
}