package flaskrtest.actions

import org.openqa.selenium.WebDriver

import com.kms.katalon.core.util.KeywordUtil

import flaskrtest.pages.auth.LogInPage
import flaskrtest.pages.auth.RegisterCredentialPage
import flaskrtest.pages.blog.IndexPage
import flaskrtest.data.User

class LoginAction {

	/**
	 *
	 * @param browser
	 * @param username
	 * @param password
	 * @return
	 */
	static void do_login(WebDriver browser, URL url, User user) {

		Objects.requireNonNull(browser)
		Objects.requireNonNull(url)
		Objects.requireNonNull(user)

		// now we go to the Index page
		IndexPage indexPage = new IndexPage(browser)
		indexPage.load(url)

		// ensure we are on the index page
		assert indexPage.app_header_exists()
		assert indexPage.register_anchor_exists()
		assert indexPage.login_anchor_exists()

		// we want to navigate to the Register page
		indexPage.open_register_page()

		// now we are on the Register page
		RegisterCredentialPage regPage = new RegisterCredentialPage(browser)

		// make sure we are on the Register page
		//WebUI.verifyElementPresent(RegisterCredentialPage.REGISTER_BUTTON, 3)
		assert regPage.register_button_exists()

		// we want to register a user
		regPage.type_username(user.toString())
		regPage.type_password(user.getPassword())

		// try registering the credential of the user
		regPage.do_register()

		// check if the user is already registered
		if (regPage.flash_exists()) {
			KeywordUtil.markWarning("username ${user.toString()} is already registered.")
			// we are still on the Register page
			// so we want to navigate to the Log In page
			regPage.do_login()
		}

		// now we are on the Login page
		LogInPage loginPage = new LogInPage(browser)
		assert loginPage.login_button_exists()

		// now let's log in
		loginPage.type_username(user.toString())
		loginPage.type_password(user.getPassword())
		loginPage.do_login()

		// now we should be are on the index page
		// make sure if he/she has successfully logged in?
		assert indexPage.nav_span_username_exists(user.toString())
	}

}
