package com.kazurayam.uitestjava.flaskr.pom.pages.blog;

import com.kazurayam.uitestjava.flaskr.pom.data.User;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class IndexPage {
	/**
	 * @param index 1,2,3, ...
	 */
	public static By POST_BY_INDEX(final int index) {
		return By.xpath(String.format(
				"//article[@class='post' and position()=%d]", index));
	}

	public static By POST_BY_POSTID(final String id) {
		return By.xpath(String.format(
				"//article[@class='post']/header/" +
						"a[starts-with(@href, '/%s')]/" +
						"ancestor::article", id));
	}

	public IndexPage(WebDriver browser) {
		this.browser = browser;
	}

	public void load(URL url) {
		browser.navigate().to(url.toExternalForm());
	}

	public void open_register_page() {
		WebElement e = browser.findElement(REGISTER_ANCHOR);
		e.click();
	}

	public void open_login_page() {
		WebElement e = browser.findElement(LOGIN_ANCHOR);
		e.click();
	}

	public void click_logout() {
		WebElement e = browser.findElement(LOGOUT_ANCHOR);
		e.click();
	}

	public Boolean app_header_exists() {
		WebDriverWait wait = new WebDriverWait(browser, TIMEOUT);
		WebElement e = wait.until(ExpectedConditions.elementToBeClickable(APP_HEADER));
		return e != null;
	}

	public Boolean register_anchor_exists() {
		WebDriverWait wait = new WebDriverWait(browser, TIMEOUT);
		WebElement e = wait.until(ExpectedConditions.elementToBeClickable(REGISTER_ANCHOR));
		return e !=null;
	}

	public Boolean login_anchor_exists() {
		WebDriverWait wait = new WebDriverWait(browser, TIMEOUT);
		WebElement e = wait.until(ExpectedConditions.elementToBeClickable(LOGIN_ANCHOR));
		return e != null;
	}

	public Boolean logout_anchor_exists() {
		WebDriverWait wait = new WebDriverWait(browser, TIMEOUT);
		WebElement e = wait.until(ExpectedConditions.elementToBeClickable(LOGOUT_ANCHOR));
		return e != null;
	}

	public Boolean posts_header_exists() {
		WebDriverWait wait = new WebDriverWait(browser, TIMEOUT);
		WebElement e = wait.until(ExpectedConditions.elementToBeClickable(POSTS_HEADER));
		return e != null;
	}

	public Boolean nav_span_username_exists(final String username) {
		WebDriverWait wait = new WebDriverWait(browser, TIMEOUT);
		By by = By.xpath(String.format("//nav/ul/li/span[text()='%s']", username));
		WebElement e = wait.until(ExpectedConditions.elementToBeClickable(by));
		return e != null;
	}

	public void open_create_post_page() {
		WebElement e = browser.findElement(NEW_ANCHOR);
		e.click();
	}

	public int get_posts_count() {
		List<WebElement> posts = browser.findElements(POSTS);
		return posts.size();
	}

	public void open_update_page_of_latest() {
		open_update_page_by_index(1);
	}

	public void open_update_page_by_index(final int index) {
		WebElement article = browser.findElement(POST_BY_INDEX(index));
		if (article != null) {
			open_update_page(article);
		} else {
			throw new IllegalArgumentException("no <article> found; index: " + index);
		}

	}

	public void open_update_page_by_postid(final String postid) {
		WebElement article = browser.findElement(POST_BY_POSTID(postid));
		if (article != null) {
			open_update_page(article);
		} else {
			throw new IllegalArgumentException("no <article> found; postid: " + postid);
		}

	}

	public static void open_update_page(WebElement article) {
		Objects.requireNonNull(article);
		WebElement anchor = article.findElement(By.xpath("//a[contains(text(), 'Edit')]"));
		if (anchor != null) {
			anchor.click();
		} else {
			throw new IllegalArgumentException("no <a>Edit</a> found");
		}

	}

	public Post get_post_latest() {
		return get_post_by_index(1);
	}

	public Post get_post_by_index(int index) {
		WebElement article = browser.findElement(POST_BY_INDEX(index));
		if (article != null) {
			return new Post(article);
		} else {
			return null;
		}

	}

	public Post get_post_by_postid(String postid) {
		Objects.requireNonNull(postid);
		WebElement article = browser.findElement(POST_BY_POSTID(postid));
		if (article != null) {
			return new Post(article);
		} else {
			return null;
		}

	}

	public List<Post> get_posts() {
		List<WebElement> articleElementList = browser.findElements(POSTS);
		return articleElementList.stream()
				.map(webElement -> { return new Post(webElement); })
				.collect(Collectors.toList());
	}

	public List<Post> get_posts_by(final User user) {
		Objects.requireNonNull(user);
		List<WebElement> posts = browser.findElements(POSTS);
		Predicate<Post> aboutContainsUser =
				post -> { return post.get_about().contains(user.toString()); };
		return posts.stream()
				.map(webElement -> { return new Post(webElement); })
				.filter(aboutContainsUser)
				.collect(Collectors.toList());
	}

	public URL get_url() throws MalformedURLException {
		String url = browser.getCurrentUrl();
		return new URL(url);
	}

	public static By getAPP_HEADER() {
		return APP_HEADER;
	}

	public static By getREGISTER_ANCHOR() {
		return REGISTER_ANCHOR;
	}

	public static By getLOGIN_ANCHOR() {
		return LOGIN_ANCHOR;
	}

	public static By getLOGOUT_ANCHOR() {
		return LOGOUT_ANCHOR;
	}

	public static By getPOSTS_HEADER() {
		return POSTS_HEADER;
	}

	public static By getNEW_ANCHOR() {
		return NEW_ANCHOR;
	}

	public static By getPOSTS() {
		return POSTS;
	}

	public static int getTIMEOUT() {
		return TIMEOUT;
	}

	private static final By APP_HEADER = By.xpath("//h1[contains(text(),\"Flaskr\")]");
	private static final By REGISTER_ANCHOR = By.xpath("//a[contains(text(), \"Register\")]");
	private static final By LOGIN_ANCHOR = By.xpath("//a[contains(text(), \"Log In\")]");
	private static final By LOGOUT_ANCHOR = By.xpath("//a[contains(text(), \"Log Out\")]");
	private static final By POSTS_HEADER = By.xpath("//h1[contains(text(), \"Posts\")]");
	private static final By NEW_ANCHOR = By.xpath("//a[contains(text(), \"New\")]");
	private static final By POSTS = By.xpath("//article[@class=\"post\"]");
	private static final int TIMEOUT = 3;
	private final WebDriver browser;
}
