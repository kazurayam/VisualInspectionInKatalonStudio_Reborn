package com.kazurayam.uitestjava.flaskr.pom.pages.auth;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.net.URL;

public class LogInPage {

	public LogInPage(WebDriver browser) {
		this.browser = browser;
	}

	public Boolean login_button_exists() {
		WebElement login_button = browser.findElement(getLOGIN_BUTTON());
		return login_button != null;
	}

	public void type_username(String username) {
		WebElement e = browser.findElement(getUSERNAME_INPUT());
		e.sendKeys(username);
	}

	public void type_password(String password) {
		WebElement e = browser.findElement(getPASSWORD_INPUT());
		e.sendKeys(password);
	}

	public void do_login() {
		WebElement e = browser.findElement(getLOGIN_BUTTON());
		e.click();
	}

	public URL get_url() throws MalformedURLException {
		String url = browser.getCurrentUrl();
		return new URL(url);
	}

	public static By getUSERNAME_INPUT() {
		return USERNAME_INPUT;
	}

	public static By getPASSWORD_INPUT() {
		return PASSWORD_INPUT;
	}

	public static By getLOGIN_BUTTON() {
		return LOGIN_BUTTON;
	}

	private static final By USERNAME_INPUT = By.id("username");
	private static final By PASSWORD_INPUT = By.id("password");
	private static final By LOGIN_BUTTON = By.xpath("//input[@type=\"submit\" and @value=\"Log In\"]");
	private final WebDriver browser;
}
