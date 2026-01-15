package com.securebank.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProfilePage extends BasePage {
    public ProfilePage(WebDriver driver) {
        super(driver);
    }

    private By firstNameInput = By.id("first-name");
    private By lastNameInput = By.id("last-name");
    private By emailInput = By.id("email");
    private By phoneInput = By.id("phone");
    private By addressInput = By.id("address");
    private By saveProfileButton = By.id("save-profile");

    public void setFirstName(String firstName) {
        sendKeys(firstNameInput, firstName);
    }

    public void setLastName(String lastName) {
        sendKeys(lastNameInput, lastName);
    }

    public void setEmail(String email) {
        sendKeys(emailInput, email);
    }

    public void setPhone(String phone) {
        sendKeys(phoneInput, phone);
    }

    public void setAddress(String address) {
        sendKeys(addressInput, address);
    }

    public void clickSaveProfile() {
        click(saveProfileButton);
    }

    public String getFirstName() {
        return waitForVisibility(firstNameInput).getAttribute("value");
    }

    public String getLastName() {
        return waitForVisibility(lastNameInput).getAttribute("value");
    }

    public String getEmail() {
        return waitForVisibility(emailInput).getAttribute("value");
    }

    public String getPhone() {
        return waitForVisibility(phoneInput).getAttribute("value");
    }

    public String getAddress() {
        return waitForVisibility(addressInput).getAttribute("value");
    }

	public void navigateToProfilePage() {
		click(By.id("nav-profile"));
        wait.until(driver -> driver.getCurrentUrl().contains("profile.html"));
	}
}