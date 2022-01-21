package flaskrtest.actions

import org.openqa.selenium.WebDriver

import com.kazurayam.ks.visualinspection.MaterializingContext

import flaskrtest.data.Song
import flaskrtest.data.User
import flaskrtest.pages.blog.CreatePostPage
import flaskrtest.pages.blog.IndexPage

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

public class PostAction {

	static void new_post(WebDriver browser, URL url, User user, Song song) {
		new_post(browser, url, song, MaterializingContext.NULL_OBJECT)
	}

	/**
	 *
	 * @param browser
	 * @param username
	 * @param song
	 * @return
	 */
	static void new_post(WebDriver browser, URL url, User user, Song song, MaterializingContext matz) {
		Objects.requireNonNull(browser)
		Objects.requireNonNull(url)
		Objects.requireNonNull(song)
		Objects.requireNonNull(matz)

		// let's start from the index page
		IndexPage indexPage = new IndexPage(browser)
		indexPage.load(url)

		// we want to navigate to the CreatePost page
		indexPage.open_create_post_page()

		// now we are on the CreatePost page
		CreatePostPage createPage = new CreatePostPage(browser)
		assert createPage.save_button_exists()

		// take screenshot
		matz.materialize(url, ["step": "4"])
		WebUI.comment("step4 ${url.toString()}")
		

		// type in the title
		String title = song.title + " --- " + song.by
		createPage.type_title(title)

		// type in the body
		createPage.type_body(song.lyric)

		// take screenshot
		matz.materialize(url, ["step": "5"])
		WebUI.comment("step5 ${url.toString()}")
		
		// save the post
		createPage.do_save()

		// now we are on the index page
		// make sure that the 1st article is the song just posted by the user
		assert indexPage.get_post_latest().get_title() == title
		assert indexPage.get_post_latest().get_about().contains(user.toString())
		assert indexPage.get_post_latest().get_body() == song.lyric

		// take screenshot
		matz.materialize(url, ["step": "6"])
		WebUI.comment("step6 ${url.toString()}")		
	}
}
