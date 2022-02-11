package com.kazurayam.uitestjava.flaskr.pom.actions;

import com.kazurayam.uitestjava.flaskr.pom.pages.blog.IndexPage;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;

public class LogoutAction extends Action {

	public LogoutAction() {}

	public void do_logout(WebDriver browser, URL startAt) throws MalformedURLException {
		do_logout(browser, startAt, new ActionListenerBaseImpl());
	}

	public void do_logout(WebDriver browser, URL startAt, ActionListener listener) throws MalformedURLException {
		Objects.requireNonNull(browser);
		Objects.requireNonNull(startAt);

		// now we go to the Index page
		IndexPage indexPage = new IndexPage(browser);
		indexPage.load(startAt);

		assert indexPage.logout_anchor_exists();

		indexPage.click_logout();

		// notify progress
		listener.on(PostAction.class, new URL(browser.getCurrentUrl()),
				Collections.singletonMap("step", "7"));
	}

}
