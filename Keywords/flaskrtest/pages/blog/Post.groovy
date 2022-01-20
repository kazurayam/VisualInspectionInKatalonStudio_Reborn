package flaskrtest.pages.blog

import org.openqa.selenium.By as SeleniumBy
import org.openqa.selenium.WebElement
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import com.google.gson.Gson
import com.google.gson.GsonBuilder

/**
 * wraps an blog post page in the Flaskr web app.
 * provides accessor methods to title and body
 * 
 <article class="post">
 <header>
 <div>
 <h1>東京はもうすぐ春</h1>
 <div class="about">by kazurayam on 2021-02-10</div>
 </div>
 <a class="action" href="/2/update">Edit</a>
 </header>
 <p class="body">どじょうが泳いでいます</p>
 </article>
 */
public class Post {

	private static Logger logger_ = LoggerFactory.getLogger(getClass())

	static final SeleniumBy TITLE = SeleniumBy.xpath('header/div/h1')
	static final SeleniumBy BODY  = SeleniumBy.xpath('p[1]')
	static final SeleniumBy EDIT  = SeleniumBy.xpath('header/a')
	static final SeleniumBy ABOUT = SeleniumBy.xpath('header/div/div[contains(@class,"about")]')

	WebElement article

	Post(WebElement article) {
		this.article = article
	}

	String get_title() {
		WebElement title = article.findElement(TITLE)
		return title.getText()
	}

	String get_body() {
		WebElement body = article.findElement(BODY)
		return body.getText()
	}

	String get_about() {
		WebElement about = article.findElement(ABOUT)
		return about.getText()
	}

	String get_postid() {
		List<WebElement> anchors = article.findElements(EDIT)
		if (anchors.size() > 0) {
			WebElement anchor = article.findElement(EDIT)
			if (anchor != null) {
				String href = anchor.getAttribute('href')   // something like "http://127.0.0.1/XX/update" where XX is an integer
				URL link = new URL(href)
				String postid = link.getPath().split('/')[1]
				return (postid != null) ? postid : ''
			} else {
				return ''
			}
		} else {
			return ''
		}
	}

	Boolean about_text_contains(String part) {
		String about = this.get_about()
		return about.contains(part)
	}

	@Override
	String toString() {
		Map m = ["header":["title": get_title(), "about": get_about(), "id": get_postid()], "body": get_body()]
		Gson gson = new GsonBuilder().setPrettyPrinting().create()
		String json = gson.toJson(m)
		return json
	}
}
