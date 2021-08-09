package my

import org.jsoup.Jsoup
import org.jsoup.nodes.Document


public class StringUtils {


	/**
	 * return tidy HTML String using jsoup
	 *
	 * @param htmlString
	 * @return tidy HTML string
	 */
	static String tidyHtmlString(String htmlString) {
		Document doc = Jsoup.parse(htmlString);   // pretty print HTML
		return doc.toString();
	}
	
}