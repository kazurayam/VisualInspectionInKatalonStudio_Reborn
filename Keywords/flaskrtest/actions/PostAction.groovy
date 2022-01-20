package flaskrtest.actions

import org.openqa.selenium.WebDriver

import flaskrtest.data.Song
import flaskrtest.pages.blog.CreatePostPage
import flaskrtest.pages.blog.IndexPage
import flaskrtest.data.User

public class PostAction {

	/**
	 *
	 * @param browser
	 * @param username
	 * @param song
	 * @return
	 */
	static void new_post(WebDriver browser, URL url, User user, Song song) {
		// let's start from the index page
		IndexPage indexPage = new IndexPage(browser)
		indexPage.load(url)

		// we want to navigate to the CreatePost page
		indexPage.open_create_post_page()

		// now we are on the CreatePost page
		CreatePostPage createPage = new CreatePostPage(browser)
		assert createPage.save_button_exists()

		// type in the title
		String title = song.title + " --- " + song.by
		createPage.type_title(title)

		// type in the body
		createPage.type_body(song.lyric)

		// save the post
		createPage.do_save()

		// now we are on the index page
		// make sure that the 1st article is the song just posted by the user
		assert indexPage.get_post_latest().get_title() == title
		assert indexPage.get_post_latest().get_about().contains(user.toString())
		assert indexPage.get_post_latest().get_body() == song.lyric
	}
}
