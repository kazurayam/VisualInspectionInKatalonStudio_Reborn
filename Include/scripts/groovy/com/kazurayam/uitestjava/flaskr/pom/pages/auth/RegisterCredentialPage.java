package com.kazurayam.uitestjava.flaskr.pom.pages.auth;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class RegisterCredentialPage {
	public RegisterCredentialPage(WebDriver browser) {
		this.browser = browser;
	}

	public Boolean register_button_exists() {
		WebElement register_button = browser.findElement(REGISTER_BUTTON);
		return register_button != null;
	}

	public Boolean flash_exists() {
		List<WebElement> flashList = browser.findElements(DIV_FLASH);
		return flashList.size() > 0;
	}

	public void type_username(String username) {
		WebElement e = browser.findElement(USERNAME_INPUT);
		e.sendKeys(username);
	}

	public void type_password(String password) {
		WebElement e = browser.findElement(PASSWORD_INPUT);
		e.sendKeys(password);
	}

	public void do_register() {
		WebElement e = browser.findElement(REGISTER_BUTTON);
		e.click();
	}

	public void do_login() {
		WebElement e = browser.findElement(LOGIN_ANCHOR);
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

	public static By getREGISTER_BUTTON() {
		return REGISTER_BUTTON;
	}

	public static By getLOGIN_ANCHOR() {
		return LOGIN_ANCHOR;
	}

	public static By getDIV_FLASH() {
		return DIV_FLASH;
	}

	private static final By USERNAME_INPUT = By.id("username");
	private static final By PASSWORD_INPUT = By.id("password");
	private static final By REGISTER_BUTTON = By.xpath("//input[@type=\"submit\" and @value=\"Register\"]");
	private static final By LOGIN_ANCHOR = By.xpath("//a[contains(text(), \"Log In\")]");
	private static final By DIV_FLASH = By.xpath("//div[contains(@class, \"flash\")]");
	private final WebDriver browser;
}
