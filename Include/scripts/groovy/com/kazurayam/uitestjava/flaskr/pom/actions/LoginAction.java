package com.kazurayam.uitestjava.flaskr.pom.actions;

import com.kazurayam.uitestjava.flaskr.pom.pages.auth.RegisterCredentialPage;
import com.kazurayam.uitestjava.flaskr.pom.pages.blog.IndexPage;
import com.kazurayam.uitestjava.flaskr.pom.data.User;
import com.kazurayam.uitestjava.flaskr.pom.pages.auth.LogInPage;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;

public class LoginAction extends Action {

	private final Logger logger;

	public LoginAction() {
		logger = LoggerFactory.getLogger(this.getClass());
	}

	/**
	 *
	 */
	public void do_login(WebDriver browser, URL startAt, final User user)
			throws MalformedURLException
	{
		do_login(browser, startAt, user, new ActionListenerBaseImpl());
	}

	/**
	 *
	 */
	public void do_login(WebDriver browser, URL startAt, final User user, ActionListener listener)
			throws MalformedURLException
	{
		Objects.requireNonNull(browser);
		Objects.requireNonNull(startAt);
		Objects.requireNonNull(user);

		// now we go to the Index page
		IndexPage indexPage = new IndexPage(browser);
		indexPage.load(startAt);

		// ensure we are on the index page
		assert indexPage.app_header_exists();
		assert indexPage.register_anchor_exists();
		assert indexPage.login_anchor_exists();

		// notify progress
		listener.on(LoginAction.class, new URL(browser.getCurrentUrl()),
				Collections.singletonMap("step", "1"));

		// we want to navigate to the Register page
		indexPage.open_register_page();

		// now we are on the Register page
		RegisterCredentialPage regPage = new RegisterCredentialPage(browser);

		// make sure we are on the Register page
		//WebUI.verifyElementPresent(RegisterCredentialPage.REGISTER_BUTTON, 3)
		assert regPage.register_button_exists();

		// we want to register a user
		regPage.type_username(user.toString());
		regPage.type_password(user.getPassword());


		// notify progress
		listener.on(LoginAction.class, new URL(browser.getCurrentUrl()),
				Collections.singletonMap("step", "2"));


		// try registering the credential of the user
		regPage.do_register();

		// check if the user is already registered
		if (regPage.flash_exists()) {
			logger.warn("username " + user + " is already registered.");
			// we are still on the Register page
			// we want to navigate to the Login page
			regPage.do_login();
		}


		// now we are on the Login page
		LogInPage loginPage = new LogInPage(browser);
		assert loginPage.login_button_exists();

		// now let's log in
		loginPage.type_username(user.toString());
		loginPage.type_password(user.getPassword());

		// notify progress
		listener.on(LoginAction.class, new URL(browser.getCurrentUrl()),
				Collections.singletonMap("step", "3"));

		loginPage.do_login();

		// now we should be on the index page
		// make sure if he/she has successfully logged in?
		assert indexPage.nav_span_username_exists(user.toString());
	}
}
