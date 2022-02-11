package com.kazurayam.uitestjava.flaskr.pom.pages.blog;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.net.URL;

public class CreatePostPage {
	public CreatePostPage(WebDriver browser) {
		this.browser = browser;
	}

	public URL get_url() throws MalformedURLException {
		String url = browser.getCurrentUrl();
		return new URL(url);
	}

	public Boolean save_button_exists() {
		WebElement save_button = browser.findElement(SAVE_BUTTON);
		return save_button != null;
	}

	public void type_title(String title) {
		WebElement e = browser.findElement(TITLE_INPUT);
		e.clear();
		e.sendKeys(title);
	}

	public void type_body(String body) {
		WebElement e = browser.findElement(BODY_INPUT);
		e.clear();
		e.sendKeys(body);
	}

	public void do_save() {
		WebElement e = browser.findElement(SAVE_BUTTON);
		e.click();
	}

	public static By getTITLE_INPUT() {
		return TITLE_INPUT;
	}

	public static By getBODY_INPUT() {
		return BODY_INPUT;
	}

	public static By getSAVE_BUTTON() {
		return SAVE_BUTTON;
	}

	private static final By TITLE_INPUT = By.id("title");
	private static final By BODY_INPUT = By.id("body");
	private static final By SAVE_BUTTON = By.xpath("//input[@type=\"submit\" and @value=\"Save\"]");
	private final WebDriver browser;
}
