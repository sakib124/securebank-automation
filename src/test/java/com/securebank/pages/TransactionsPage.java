package com.securebank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import java.util.List;

public class TransactionsPage extends BasePage {
	public TransactionsPage(WebDriver driver) {
		super(driver);
	}

	private By filterAccountSelect = By.id("filter-account");
	private By filterTypeSelect = By.id("filter-type");
	private By filterDateFrom = By.id("filter-date-from");
	private By filterDateTo = By.id("filter-date-to");
	private By applyFiltersButton = By.id("apply-filters");
	private By resetFiltersButton = By.id("reset-filters");
	private By transactionsBody = By.id("full-transactions-body");
	private By prevPageButton = By.id("prev-page");
	private By nextPageButton = By.id("next-page");
	private By pageInfo = By.id("page-info");
	private By transactionsCountDisplay = By.id("transactions-count");

	public void selectAccountFilter(String value) {
		getSelect(filterAccountSelect).selectByVisibleText(value);
	}

	public void selectTypeFilter(String value) {
		getSelect(filterTypeSelect).selectByVisibleText(value);
	}

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

	public void applyFilters() {
		click(applyFiltersButton);
	}

	public void resetFilters() {
		click(resetFiltersButton);
	}

	public List<WebElement> getTransactionsRows() {
		return waitForVisibility(transactionsBody).findElements(By.tagName("tr"));
	}

	public void clickNextPage() {
		click(nextPageButton);
	}

	public void clickPrevPage() {
		click(prevPageButton);
	}

	public String getPageInfo() {
		return getText(pageInfo);
	}

	public boolean hasTransaction(String date, String description, String amount, String from, String to,
			String status) {
		List<WebElement> rows = getTransactionsRows();
		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			if (cells.size() >= 6) {
				String rowDate = cells.get(0).getText().trim();
				String rowDescription = cells.get(1).getText().trim();
				String rowAmount = cells.get(2).getText().trim();
				String rowFrom = cells.get(3).getText().trim();
				String rowTo = cells.get(4).getText().trim();
				String rowStatus = cells.get(5).getText().trim();
				if (rowDate.equals(date) && rowDescription.equals(description) && rowAmount.equals(amount)
						&& rowFrom.equalsIgnoreCase(from) && rowTo.equalsIgnoreCase(to) && rowStatus.equals(status)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Checks if the first two rows in the transactions table match the expected
	 * transfer details for 'from' and 'to' accounts (order independent).
	 */
	public boolean hasTransferRows(String description, String amount, String from, String to, String status) {
		List<WebElement> rows = getTransactionsRows();
		if (rows.size() < 2) {
			return false;
		}
		WebElement row1 = rows.get(0);
		WebElement row2 = rows.get(1);
		List<WebElement> cells1 = row1.findElements(By.tagName("td"));
		List<WebElement> cells2 = row2.findElements(By.tagName("td"));
		if (cells1.size() < 8 || cells2.size() < 8) {
			return false;
		}
		String fromNorm = from.trim().toLowerCase();
		String toNorm = to.trim().toLowerCase();
		String expectedAmount = amount.replace("$", "").replace(",", "").trim();
		// Helper to check a row for a given account
		java.util.function.BiPredicate<List<WebElement>, String> rowMatches = (cells, expectedAccount) -> {
			String rowId = cells.get(0).getText().trim();
			String rowAccount = cells.get(2).getText().trim();
			String rowType = cells.get(3).getText().trim();
			String rowDescription = cells.get(4).getText().trim();
			String rowAmount = cells.get(5).getText().replace("$", "").replace(",", "").trim();
			String rowStatus = cells.get(7).getText().trim();
			boolean match = rowId.contains("TXN") && rowAccount.trim().toLowerCase().equals(expectedAccount)
					&& rowType.equalsIgnoreCase("transfer") && rowDescription.equals(description)
					&& rowAmount.equals(expectedAmount) && rowStatus.equalsIgnoreCase(status);
			return match;
		};
		boolean foundFrom = rowMatches.test(cells1, fromNorm) || rowMatches.test(cells2, fromNorm);
		boolean foundTo = rowMatches.test(cells1, toNorm) || rowMatches.test(cells2, toNorm);
		return foundFrom && foundTo;
	}

	public boolean allRowsMatchAccount(String account) {
		if (account.equalsIgnoreCase("Credit Card")) {
			account = "Credit";
		}
		List<WebElement> rows = getTransactionsRows();
		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			if (cells.size() < 4)
				return false;
			String rowAccount = cells.get(2).getText().trim();
			if (!rowAccount.equalsIgnoreCase(account))
				return false;
		}
		return true;
	}

	public boolean allRowsMatchType(String type) {
		List<WebElement> rows = getTransactionsRows();
		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			if (cells.size() < 5)
				return false;
			String rowType = cells.get(3).getText().trim();
			if (!rowType.equalsIgnoreCase(type))
				return false;
		}
		return true;
	}

	public boolean allRowsWithinDateRange(String from, String to) {
		List<WebElement> rows = getTransactionsRows();
		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			if (cells.size() < 2)
				return false;
			String date = cells.get(1).getText().trim();
			if (date.compareTo(from) < 0 || date.compareTo(to) > 0)
				return false;
		}
		return true;
	}

	public boolean allRowsMatchAccountAndType(String account, String type) {
		List<WebElement> rows = getTransactionsRows();
		for (WebElement row : rows) {
			List<WebElement> cells = row.findElements(By.tagName("td"));
			if (cells.size() < 5)
				return false;
			String rowAccount = cells.get(2).getText().trim();
			String rowType = cells.get(3).getText().trim();
			if (!rowAccount.equalsIgnoreCase(account) || !rowType.equalsIgnoreCase(type))
				return false;
		}
		return true;
	}

	public boolean filtersAreDefault() {
		Select accountDropdown = new Select(
				wait.until(ExpectedConditions.visibilityOfElementLocated(filterAccountSelect)));
		Select typeDropdown = new Select(wait.until(ExpectedConditions.visibilityOfElementLocated(filterTypeSelect)));
		String account = accountDropdown.getFirstSelectedOption().getText().trim();
		String type = typeDropdown.getFirstSelectedOption().getText().trim();
		String dateFrom = wait.until(ExpectedConditions.visibilityOfElementLocated(filterDateFrom))
				.getAttribute("value");
		String dateTo = wait.until(ExpectedConditions.visibilityOfElementLocated(filterDateTo)).getAttribute("value");
		boolean isDefault = (account.equalsIgnoreCase("All Accounts") || account.isEmpty())
				&& (type.equalsIgnoreCase("All Types") || type.isEmpty()) && (dateFrom.isEmpty() && dateTo.isEmpty());
		return isDefault;
	}

	public String checkForNoResults() {
		return wait.until(ExpectedConditions.visibilityOfElementLocated(transactionsCountDisplay)).getText();
	}

	public boolean hasPagination() {
		// Checks if next/prev page buttons are enabled or visible
		boolean nextEnabled = false;
		boolean prevEnabled = false;
		try {
			WebElement next = wait.until(ExpectedConditions.visibilityOfElementLocated(nextPageButton));
			nextEnabled = next.isEnabled();
		} catch (Exception e) {
			/* ignore */ }
		try {
			WebElement prev = wait.until(ExpectedConditions.visibilityOfElementLocated(prevPageButton));
			prevEnabled = prev.isEnabled();
		} catch (Exception e) {
			/* ignore */ }
		return nextEnabled || prevEnabled;
	}

	public boolean isNextPageDisplayed() {
		// Example: check that page info or table content changes after clicking next
		String before = getPageInfo();
		clickNextPage();
		String after = getPageInfo();
		return !before.equals(after);
	}

	public boolean isPreviousPageDisplayed() {
		// Example: check that page info or table content changes after clicking
		// previous
		String before = getPageInfo();
		clickPrevPage();
		String after = getPageInfo();
		return !before.equals(after);
	}

	public boolean isNextPageEnabled() {
		WebElement next = wait.until(ExpectedConditions.visibilityOfElementLocated(nextPageButton));
		return next.isEnabled();
	}

	public boolean isPrevPageEnabled() {
		WebElement prev = wait.until(ExpectedConditions.visibilityOfElementLocated(prevPageButton));
		return prev.isEnabled();
	}

	public void navigateToTransactionsPage() {
		click(By.id("nav-transactions"));
		wait.until(driver -> driver.getCurrentUrl().contains("transactions.html"));
	}

	public void setDateRange(String from, String to) {
		sendKeys(filterDateFrom, from);
		sendKeys(filterDateFrom, to);
	}
}
