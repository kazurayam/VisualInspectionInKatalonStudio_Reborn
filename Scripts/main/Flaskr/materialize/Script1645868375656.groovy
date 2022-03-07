import org.openqa.selenium.WebDriver

import com.kazurayam.ks.visualinspection.StoreMaterialActionListener
import com.kazurayam.uitestjava.flaskr.pom.actions.LoginAction
import com.kazurayam.uitestjava.flaskr.pom.actions.LogoutAction
import com.kazurayam.uitestjava.flaskr.pom.actions.PostAction
import com.kazurayam.uitestjava.flaskr.pom.data.Song
import com.kazurayam.uitestjava.flaskr.pom.data.Songs
import com.kazurayam.uitestjava.flaskr.pom.data.User
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

/**
 * Test Cases/main/Flaskr/materialize
 *
 */
// check params which should be passed as the arguments of WebUI.callTestCases() call
Objects.requireNonNull(profile)
Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)

StoreMaterialActionListener matz = new StoreMaterialActionListener(profile, store, jobName, jobTimestamp)

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
LoginAction loginAction = new LoginAction()
loginAction.do_login(browser, indexUrl, user, matz)

// the user makes a post with a song
PostAction postAction = new PostAction()
postAction.new_post(browser, indexUrl, user, song, matz)

// the user logs out
LogoutAction logoutAction = new LogoutAction()
logoutAction.do_logout(browser, indexUrl, matz)

browser.quit()