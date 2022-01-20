import org.openqa.selenium.Dimension
import org.openqa.selenium.Point
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver

import com.kms.katalon.core.webui.driver.DriverFactory

import flaskrtest.actions.LoginAction
import flaskrtest.actions.LogoutAction
import flaskrtest.actions.PostAction
import flaskrtest.data.Song
import flaskrtest.data.Songs
import flaskrtest.data.User
import flaskrtest.pages.blog.IndexPage
import flaskrtest.pages.blog.Post

import static com.kazurayam.ks.Assert.*

// preparation for ChromeDriver
String chrome_executable_path = DriverFactory.getChromeDriverPath()
System.setProperty('webdriver.chrome.driver', chrome_executable_path)

// open a Chome browser for Alice
WebDriver browser0 = new ChromeDriver()
layoutWindow(browser0, new Dimension(720, 800), new Point(0, 0))

// open a Chrome browser for Bob
WebDriver browser1 = new ChromeDriver()
layoutWindow(browser1, new Dimension(720, 800), new Point(720, 0))

URL indexUrl = new URL('http://127.0.0.1:3080/')

// Alice logs in
LoginAction.do_login(browser0, indexUrl, User.Alice)

// Bob logs in
LoginAction.do_login(browser1, indexUrl, User.Bob)

Song song_of_miyuki = Songs.get(0)
Song song_of_queen  = Songs.get(1)

// Alice makes a post with a song by Miyuki Nakajima
PostAction.new_post(browser0, indexUrl, User.Alice, song_of_miyuki)

// Bob makes a post with a song by Queen
PostAction.new_post(browser1, indexUrl, User.Bob, song_of_queen)

// ensure Alice finds the song that Bob posted
checkIfPostBySomebodyPresent(browser0, indexUrl, User.Alice, User.Bob, song_of_queen)

// ensure Bob finds the song that Alice posted
checkIfPostBySomebodyPresent(browser1, indexUrl, User.Bob, User.Alice, song_of_miyuki)

// logout
LogoutAction.do_logout(browser0, indexUrl)
LogoutAction.do_logout(browser1, indexUrl)


// close 2 browsers
browser0.quit()
browser1.quit()


/**
 *
 * @param browser
 * @param username
 * @param somebody
 * @param song
 * @return
 */
def checkIfPostBySomebodyPresent(WebDriver browser, URL url, User me, User somebody, Song song) {
	// let's start from the index page
	IndexPage indexPage = new IndexPage(browser)
	indexPage.load(url)

	// find a post by somebody
	List<Post> postsBySomebody = indexPage.get_posts_by(somebody)

	assertTrue("indexPage.get_posts_by(${somebody}) returned 0",
		postsBySomebody.size() > 0)

	String foundTitle = postsBySomebody.get(0).get_title()
	assertTrue("foundTitle is null",
		foundTitle != null)

	assertTrue("${me} expected to find a post by ${somebody} with a song \"${song.title}\" but got \"${foundTitle}\"",
		foundTitle.contains(song.title))

}

/**
 *
 * @param browser
 * @param dimension
 * @param point
 * @return
 */
def layoutWindow(WebDriver browser, Dimension dimension, Point point) {
	browser.manage().window().setSize(dimension)
	browser.manage().window().setPosition(point)
}
