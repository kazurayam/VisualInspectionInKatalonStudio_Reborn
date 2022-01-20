package flaskrtest.pages.auth

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement

import com.kazurayam.ks.testobject.By
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

public class LogInPage {

	static final TestObject USERNAME_INPUT  = By.id('username')
	static final TestObject PASSWORD_INPUT  = By.id('password')
	static final TestObject LOGIN_BUTTON = By.xpath('//input[@type="submit" and @value="Log In"]')

	private WebDriver browser

	LogInPage(WebDriver browser) {
		this.browser = browser
	}

	Boolean login_button_exists() {
		DriverFactory.changeWebDriver(browser)
		WebElement login_button = WebUI.findWebElement(LOGIN_BUTTON)
		return login_button != null
	}

	void type_username(String username) {
		DriverFactory.changeWebDriver(browser)
		WebUI.sendKeys(USERNAME_INPUT, username)
	}

	void type_password(String password) {
		DriverFactory.changeWebDriver(browser)
		WebUI.sendKeys(PASSWORD_INPUT, password)
	}

	void do_login() {
		DriverFactory.changeWebDriver(browser)
		WebUI.click(LOGIN_BUTTON)
	}
}
