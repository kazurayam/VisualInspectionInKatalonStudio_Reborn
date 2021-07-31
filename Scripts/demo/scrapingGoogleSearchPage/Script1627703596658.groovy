import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver

import com.kazurayam.materialstore.store.FileType
import com.kazurayam.materialstore.store.JobName
import com.kazurayam.materialstore.store.JobTimestamp
import com.kazurayam.materialstore.store.Material
import com.kazurayam.materialstore.store.Metadata
import com.kazurayam.materialstore.store.MetadataPattern
import com.kazurayam.materialstore.store.Store
import com.kazurayam.materialstore.store.Stores
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import my.MetadataUtils

Path projectDir = Paths.get(RunConfiguration.getProjectDir())
Path root = projectDir.resolve("Materials")
Store store = Stores.newInstance(root)
JobName jobName = new JobName("scrapingGoogleSearchPage")
JobTimestamp jobTimestamp = JobTimestamp.now()

String startingURL = "https://www.google.com/"
WebUI.openBrowser("")
WebUI.navigateToUrl(startingURL)

// <input> element for search keywords
TestObject q = new TestObject("q")
q.addProperty("xpath", ConditionType.EQUALS, "//input[@name='q']")

WebUI.verifyElementPresent(q, 10)
WebUI.setText(q, "katalon")

// now store the screenshot and the HTML source of the search page
URL searchPageURL = new URL(WebUI.getUrl())
Metadata metadata1 = new Metadata([
	"URL.host": searchPageURL.getHost(),  // "www.google.com"
	"URL.file": searchPageURL.getFile()   // "/"
	])
takeFullPageScreenshotAndSavePageSourceUsingBuiltinKeyword(store, jobName, jobTimestamp, metadata1)

// send ENTER to trasfer to the result page
WebUI.sendKeys(q, Keys.chord(Keys.ENTER))

// logo image
TestObject logo = new TestObject("logo")
logo.addProperty("xpath", ConditionType.EQUALS, "//div[@class='logo']/a/img")

// wait for the search result page is loaded
WebUI.verifyElementPresent(logo, 10)

// now store the screenshot and the HTML source of the result page
URL resultPageURL = new URL(WebUI.getUrl())
Metadata metadata2 = new Metadata([
	"URL.host": resultPageURL.getHost(),  // "www.google.com"
	"URL.file": resultPageURL.getFile()   // "/search?q=katalon&sxsrf=..."
	])
takeFullPageScreenshotAndSavePageSourceUsingBuiltinKeyword(store, jobName, jobTimestamp, metadata2)

// well, the URL of the result page is so long and complicated. I want to make the Metadata more compact.
Metadata metadata3 = new Metadata([
	"URL.host": resultPageURL.getHost(),
	"URL.file_without_query": MetadataUtils.substringBefore(resultPageURL.getFile(), "?"),
	"URL.query_q": MetadataUtils.getQueryValue(resultPageURL.getQuery(), "q")
	])
takeFullPageScreenshotAndSavePageSourceUsingBuiltinKeyword(store, jobName, jobTimestamp, metadata3)

WebUI.closeBrowser()

// compile a list of Materials in HTML
List<Material> materials = store.select(jobName, jobTimestamp, MetadataPattern.ANY)

Path report = store.reportMaterials(jobName, materials, jobName.toString() + ".html")



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
	String pageSource = driver.getPageSource()
	Material html = store.write(jobName, jobTimestamp, FileType.HTML, metadata, pageSource)
	assert html != null
}

