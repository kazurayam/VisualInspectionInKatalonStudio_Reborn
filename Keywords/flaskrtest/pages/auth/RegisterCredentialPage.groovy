package flaskrtest.pages.auth

import org.openqa.selenium.By as SeleniumBy
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kazurayam.ks.testobject.By
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

public class RegisterCredentialPage {

	static final TestObject USERNAME_INPUT  = By.id('username')
	static final TestObject PASSWORD_INPUT  = By.id('password')
	static final TestObject REGISTER_BUTTON = By.xpath('//input[@type="submit" and @value="Register"]')
	static final TestObject LOGIN_ANCHOR    = By.xpath('//a[contains(text(), "Log In")]')
	static final SeleniumBy DIV_FLASH       = SeleniumBy.xpath('//div[contains(@class, "flash")]')

	private WebDriver browser

	RegisterCredentialPage(WebDriver browser) {
		this.browser = browser
	}

	public Boolean register_button_exists() {
		DriverFactory.changeWebDriver(browser)
		WebElement register_button = WebUI.findWebElement(REGISTER_BUTTON)
		return register_button != null
	}

	Boolean flash_exists() {
		List<WebElement> flashList = browser.findElements(DIV_FLASH)
		return flashList.size() > 0
	}

	void type_username(String username) {
		DriverFactory.changeWebDriver(browser)
		WebUI.sendKeys(USERNAME_INPUT, username)
	}

	void type_password(String password) {
		DriverFactory.changeWebDriver(browser)
		WebUI.sendKeys(PASSWORD_INPUT, password)
	}

	void do_register() {
		DriverFactory.changeWebDriver(browser)
		WebUI.click(REGISTER_BUTTON)
	}

	void do_login() {
		DriverFactory.changeWebDriver(browser)
		WebUI.click(LOGIN_ANCHOR)
	}
}
