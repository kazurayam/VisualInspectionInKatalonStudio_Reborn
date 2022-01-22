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

// Bob logs in
LoginAction.do_login(browser, indexUrl, User.Bob, matz)

Song song_of_queen = Songs.get(1)

// Bob makes a post with a song by Queen
PostAction.new_post(browser, indexUrl, User.Bob, song_of_queen, matz)

// Bob logs out
LogoutAction.do_logout(browser, indexUrl, matz)

browser.quit()
