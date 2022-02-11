package com.kazurayam.uitestjava.flaskr.pom.actions;

import com.kazurayam.uitestjava.flaskr.pom.pages.blog.CreatePostPage;
import com.kazurayam.uitestjava.flaskr.pom.pages.blog.IndexPage;
import com.kazurayam.uitestjava.flaskr.pom.data.Song;
import com.kazurayam.uitestjava.flaskr.pom.data.User;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Objects;

public class PostAction extends Action {

	public PostAction() {}

	/**
	 *
	 */
	public void new_post(WebDriver browser, URL startAt, User user, Song song) throws MalformedURLException {
		new_post(browser, startAt, user, song,
				new ActionListenerBaseImpl());
	}

	/**
	 *
	 */
	public void new_post(WebDriver browser, URL startAt, User user, Song song, ActionListener listener) throws MalformedURLException {
		Objects.requireNonNull(browser);
		Objects.requireNonNull(startAt);
		Objects.requireNonNull(song);

		// let's start from the index page
		IndexPage indexPage = new IndexPage(browser);
		indexPage.load(startAt);

		// we want to navigate to the CreatePost page
		indexPage.open_create_post_page();

		// now we are on the CreatePost page
		CreatePostPage createPage = new CreatePostPage(browser);
		assert createPage.save_button_exists();

		// notify progress
		listener.on(PostAction.class, new URL(browser.getCurrentUrl()),
				Collections.singletonMap("step", "4"));

		// type in the title
		String title = song.getTitle() + " --- " + song.getBy();
		createPage.type_title(title);

		// type in the body
		createPage.type_body(song.getLyric());

		// notify progress
		listener.on(PostAction.class, new URL(browser.getCurrentUrl()),
				Collections.singletonMap("step", "5"));

		// save the post
		createPage.do_save();

		// now we are on the index page
		// make sure that the 1st article is the song just posted by the user
		assert indexPage.get_post_latest().get_title().equals(title);
		assert indexPage.get_post_latest().get_about().contains(user.toString());
		assert indexPage.get_post_latest().get_body().equals(song.getLyric());

		// notify progress
		listener.on(PostAction.class, new URL(browser.getCurrentUrl()),
				Collections.singletonMap("step", "6"));
	}

}
