package flaskrtest.actions

import org.openqa.selenium.WebDriver

import com.kazurayam.ks.visualinspection.MaterializingContext

import flaskrtest.pages.blog.IndexPage


public class LogoutAction {

	static void do_logout(WebDriver browser, URL url) {
		do_logout(browser, url, MaterializingContext.NULL_OBJECT)
	}

	static void do_logout(WebDriver browser, URL url, MaterializingContext matz) {
		Objects.requireNonNull(browser)
		Objects.requireNonNull(url)
		Objects.requireNonNull(matz)

		// now we go to the Index page
		IndexPage indexPage = new IndexPage(browser)
		indexPage.load(url)

		assert indexPage.logout_anchor_exists()

		indexPage.click_logout()
		
		// take screenshot
		matz.materialize(url, ["step": "7"])
		

	}
}
