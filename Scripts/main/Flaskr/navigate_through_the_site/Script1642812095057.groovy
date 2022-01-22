import org.openqa.selenium.WebDriver

import com.kazurayam.ks.visualinspection.MaterializingContext
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import flaskrtest.actions.LoginAction
import flaskrtest.actions.LogoutAction
import flaskrtest.actions.PostAction
import flaskrtest.data.Song
import flaskrtest.data.Songs
import flaskrtest.data.User
import internal.GlobalVariable

// check params which should be passed as the arguments of WebUI.callTestCases() call
Objects.requireNonNull(profile)
Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)

MaterializingContext matz = new MaterializingContext(profile, store, jobName, jobTimestamp)

WebUI.openBrowser('')
WebDriver browser = DriverFactory.getWebDriver()
WebUI.setViewPortSize(720,600)

assert GlobalVariable.URL != null, "GlobalVariable.URL is not defined"
URL indexUrl = new URL(GlobalVariable.URL)

User user = User.Bob
Song song = Songs.get(1)

/* alternatively you can try */
// User user = User.Alice
// Song song = Songs.get(0)

// the user logs in
LoginAction.do_login(browser, indexUrl, user, matz)

// the user makes a post with a song
PostAction.new_post(browser, indexUrl, user, song, matz)

// the user logs out
LogoutAction.do_logout(browser, indexUrl, matz)

browser.quit()
