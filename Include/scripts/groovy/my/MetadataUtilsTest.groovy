package my

import my.MetadataUtils
import static org.junit.Assert.*
import static org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

public class MetadataUtilsTest {

	@Test
	void test_substringBefore() {
		String source = "/search?q=katalon"
		String actual = MetadataUtils.substringBefore(source, "?")
		String expected = "/search"
		assertEquals(expected, actual)
	}
	
	@Test
	void test_getQueryValue() {
		String queryString = "q=katalon&dfe=piiipfe&cxw=fcfw"
		String expected = "katalon"
		String actual = MetadataUtils.getQueryValue(queryString, "q")
		assertEquals(expected, actual)
	}
}
