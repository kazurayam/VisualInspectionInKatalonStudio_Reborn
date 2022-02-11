package com.kazurayam.uitestjava.flaskr.pom.pages.blog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * wraps a blog post page in the Flaskr web app.
 * provides accessor methods to title and body
 *
 * <article class="post">
 * <header>
 * <div>
 * <h1>東京はもうすぐ春</h1>
 * <div class="about">by kazurayam on 2021-02-10</div>
 * </div>
 * <a class="action" href="/2/update">Edit</a>
 * </header>
 * <p class="body">どじょうが泳いでいます</p>
 * </article>
 */
public class Post {

	public Post(WebElement article) {
		this.article = article;
	}

	public String get_title() {
		WebElement title = article.findElement(TITLE);
		return title.getText();
	}

	public String get_body() {
		WebElement body = article.findElement(BODY);
		return body.getText();
	}

	public String get_about() {
		WebElement about = article.findElement(ABOUT);
		return about.getText();
	}

	public String get_postid() {
		List<WebElement> anchors = article.findElements(EDIT);
		if (anchors.size() > 0) {
			WebElement anchor = article.findElement(EDIT);
			if (anchor != null) {
				String href = anchor.getAttribute("href");// something like "http://127.0.0.1/XX/update" where XX is an integer
				try {
					URL link = new URL(href);
					String postid = link.getPath().split("/")[1];
					return (postid != null) ? postid : "";
				} catch (MalformedURLException e) {
					throw  new RuntimeException(e);
				}
			} else {
				return "";
			}

		} else {
			return "";
		}

	}

	public Boolean about_text_contains(String part) {
		String about = this.get_about();
		return about.contains(part);
	}

	@Override
	public String toString() {
		LinkedHashMap<String, Serializable> map = new LinkedHashMap<>(2);
		LinkedHashMap<String, String> map1 = new LinkedHashMap<>(3);
		map1.put("title", get_title());
		map1.put("about", get_about());
		map1.put("id", get_postid());
		map.put("header", map1);
		map.put("body", get_body());
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		return gson.toJson(map);
	}

	public static By getTITLE() {
		return TITLE;
	}

	public static By getBODY() {
		return BODY;
	}

	public static By getEDIT() {
		return EDIT;
	}

	public static By getABOUT() {
		return ABOUT;
	}

	public WebElement getArticle() {
		return article;
	}

	public void setArticle(WebElement article) {
		this.article = article;
	}

	private static final By TITLE = By.xpath("header/div/h1");
	private static final By BODY = By.xpath("p[1]");
	private static final By EDIT = By.xpath("header/a");
	private static final By ABOUT = By.xpath("header/div/div[contains(@class,\"about\")]");
	private WebElement article;
}
