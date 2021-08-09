package my

import java.nio.charset.StandardCharsets

import org.apache.http.NameValuePair

import com.kazurayam.materialstore.MetadataImpl

public class MetadataUtils {

	static String substringBefore(String source, String delimiter) {
		int position = source.indexOf(delimiter)
		return source.substring(0, position)
	}

	/**
	 *
	 * @param queryString "q=katalon&dfe=piiipfe&cxw=fcfw"
	 * @param key "q"
	 * @return "katalon"
	 */
	static String getQueryValue(String queryString, String key) {
		List<NameValuePair> pairs = MetadataImpl.parseURLQuery(queryString, StandardCharsets.UTF_8)
		String value = ""
		pairs.each { NameValuePair pair ->
			if (pair.getKey() != null && pair.getKey() == key) {
				value = pair.getValue()
				return
			}
		}
		return value
	}
}
