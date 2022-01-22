package flaskrtest.actions

import org.openqa.selenium.WebDriver

import com.kazurayam.ks.visualinspection.MaterializingContext

import flaskrtest.pages.blog.IndexPage

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI


public class LogoutAction {

	static void do_logout(WebDriver browser, URL startAt) {
		do_logout(browser, startAt, MaterializingContext.NULL_OBJECT)
	}

	static void do_logout(WebDriver browser, URL startAt, MaterializingContext matz) {
		Objects.requireNonNull(browser)
		Objects.requireNonNull(startAt)
		Objects.requireNonNull(matz)

		// now we go to the Index page
		IndexPage indexPage = new IndexPage(browser)
		indexPage.load(startAt)

		assert indexPage.logout_anchor_exists()

		indexPage.click_logout()

		// take screenshot
		// take screenshot
		URL url = new URL(WebUI.getUrl())
		matz.materialize(url, ["step": "7"])
		WebUI.comment("step7 ${url.toString()}")
	}
}
