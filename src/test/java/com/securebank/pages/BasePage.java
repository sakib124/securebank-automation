package com.securebank.pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;
import java.time.Duration;

public class BasePage {
    public WebDriver driver;
    public WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public WebElement waitForClick(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public void click(By locator) {
        waitForClick(locator).click();
    }

    public void sendKeys(By locator, String text) {
        WebElement el = waitForVisibility(locator);
        el.clear();
        el.sendKeys(text);
    }

    public void sendKeys(By locator, CharSequence... keys) {
        WebElement el = waitForVisibility(locator);
        el.clear();
        el.sendKeys(keys);
    }

    public String getText(By locator) {
        return waitForVisibility(locator).getText();
    }

    public boolean isEnabled(By locator) {
        return waitForVisibility(locator).isEnabled();
    }

    public Select getSelect(By locator) {
        return new Select(waitForVisibility(locator));
    }
}