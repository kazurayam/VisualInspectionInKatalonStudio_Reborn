import java.nio.file.Files
import java.nio.file.Path

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver

import com.kazurayam.materialstore.core.filesystem.FileType
import com.kazurayam.materialstore.core.filesystem.JobName
import com.kazurayam.materialstore.core.filesystem.JobTimestamp
import com.kazurayam.materialstore.core.filesystem.Material
import com.kazurayam.materialstore.core.filesystem.MaterialList
import com.kazurayam.materialstore.core.filesystem.Metadata
import com.kazurayam.materialstore.core.filesystem.QueryOnMetadata
import com.kazurayam.materialstore.core.filesystem.Store
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

/**
 * Test Cases/main/GoogleSearch/materialize
 * 
 */

assert store != null
assert jobName != null
assert jobTimestamp != null

String startingURL = "https://www.google.com/"

WebUI.openBrowser("")
WebUI.navigateToUrl(startingURL)

// <input> element for search keywords
TestObject q = new TestObject("q")
q.addProperty("xpath", ConditionType.EQUALS, "//input[@name='q']")

WebUI.verifyElementPresent(q, 10)
String queryValue = "katalon"
WebUI.setText(q, queryValue)

// now take the screenshot and store the PNG file of the Google Search page
URL searchPageURL = new URL(WebUI.getUrl())
Metadata metadata1 = Metadata.builder(searchPageURL).put("step", "01").build()
takeFullPageScreenshotAndSavePageSourceUsingBuiltinKeyword(store, jobName, jobTimestamp, metadata1)

// send ENTER to trasfer to the result page
WebUI.sendKeys(q, Keys.chord(Keys.ENTER))

// logo image
TestObject logo = new TestObject("logo")
logo.addProperty("xpath", ConditionType.EQUALS, "//div[contains(@class,'logo')]/a/img")

// wait for the search result page is loaded
WebUI.verifyElementPresent(logo, 10)

// now store the screenshot and the HTML source of the Search Result page
URL resultPageURL = new URL(WebUI.getUrl())
Metadata metadata2 = Metadata.builder(resultPageURL).put("step", "02").build()
takeFullPageScreenshotAndSavePageSourceUsingBuiltinKeyword(store, jobName, jobTimestamp, metadata2)

// make one more set of materials with "q":"katalon" appended into the metadata
Metadata metadata3 = Metadata.builder(resultPageURL).put("q", queryValue).put("step", "03").build()
takeFullPageScreenshotAndSavePageSourceUsingBuiltinKeyword(store, jobName, jobTimestamp, metadata3)

WebUI.closeBrowser()

// compile a list of the aterials in HTML
MaterialList materialList = store.select(jobName, jobTimestamp, QueryOnMetadata.ANY)



/**
 *
 * @param metadata
 * @return
 */
def takeFullPageScreenshotAndSavePageSourceUsingBuiltinKeyword(Store store, JobName jobName,
		JobTimestamp jobTimestamp, Metadata metadata) {
	Objects.requireNonNull(store)
	Objects.requireNonNull(jobName)
	Objects.requireNonNull(jobTimestamp)
	Objects.requireNonNull(metadata)
	// take a screenshot and save the image into a temporary file using Katalon's built-in keyword
	Path tempFile = Files.createTempFile(null, null);
	WebUI.takeFullPageScreenshot(tempFile.toAbsolutePath().toFile().toString(), [])
	// save the image file into the materialstore
	Material image = store.write(jobName, jobTimestamp, FileType.PNG, metadata, tempFile)
	assert image != null
	// save the page source HTML
	WebDriver driver = DriverFactory.getWebDriver()
	Document doc = Jsoup.parse(driver.getPageSource())
	String pageSource = doc.toString()
	Material html = store.write(jobName, jobTimestamp, FileType.HTML, metadata, pageSource)
	assert html != null
}

