import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.awt.image.BufferedImage
import java.nio.file.Files
import java.nio.file.Path
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver

import com.kazurayam.materialstore.selenium.AShotWrapper
import com.kazurayam.materialstore.selenium.DevicePixelRatioResolver
import com.kazurayam.materialstore.store.FileType
import com.kazurayam.materialstore.store.JobName
import com.kazurayam.materialstore.store.JobTimestamp
import com.kazurayam.materialstore.store.Material
import com.kazurayam.materialstore.store.Metadata
import com.kazurayam.materialstore.store.Store
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

// check params which should be passed as the arguments of WebUI.callTestCases() call
Objects.requireNonNull(profile)
Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)

// check the GlobalVariables
assert GlobalVariable.Hostname != null, "GlobalVariable.Hostname is not defined"
assert GlobalVariable.Username != null, "GlobalVariable.Username is not defined"
assert GlobalVariable.Password != null, "GlobalVariable.Password is not defined"
assert GlobalVariable.SAVE_PAGE_SOURCE != null, "GlobalVariable.SAVE_PAGE_SORCE is not defined"

WebUI.openBrowser('')
WebUI.setViewPortSize(1024, 800)

WebDriver driver = DriverFactory.getWebDriver()
assert driver != null, "could not get the WebDriver instance"

WebUI.navigateToUrl("http://${GlobalVariable.Hostname}/")
// we assume that Katalon Studio is configured to implicitly wait for the page completely loaded

// -------- The top page is open -------------------------------------- 

// take the screenshot and the page source, save them into the store; using the Katalon keyword
URL url = new URL(WebUI.getUrl())
takeScreenshotAndSavePageSourceUsingBuiltinKeyword(store, jobName, jobTimestamp, new Metadata([
	"profile": profile,          // "CURA_ProdcutionEnv" or "CURA_DevelopmentEnv"
	"URL": url.toString(),       // "https://katalon-demo-cura.herokuapp.com/"
	"URL.host": url.getHost(),   // "katalon-demo-cura.herokuapp.com"
	"URL.file": url.getFile(),   // "/"
	]))

WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/top/a_Make Appointment'))

// -------- The login page is open ------------------------------------

WebUI.verifyElementPresent(findTestObject('CURA/Page_CURA Healthcare Service/login/input_Username_username'), 10)

assert GlobalVariable.Username != null, "GlobalVariable.Username is not defined"

WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/login/input_Username_username'),
	GlobalVariable.Username)

assert GlobalVariable.Password != null, "GlobalVariable.Password is not defined"

WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/login/input_Password_password'),
	GlobalVariable.Password)

// take the screenshot and the page source, save them into the store; using the AShot library
url = new URL(WebUI.getUrl())
takeScreenshotAndSavePageSourceUsingAShot(store, jobName, jobTimestamp, new Metadata([
	"profile": profile,          // "CURA_ProdcutionEnv" or "CURA_DevelopmentEnv"
	"URL": url.toString(),       // "https://katalon-demo-cura.herokuapp.com/profile.php#login"
	"URL.host": url.getHost(),   // "katalon-demo-cura.herokuapp.com"
	"URL.file": url.getFile(),   // "/profile.php#login"
	]))

WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/login/button_Login'))

// -------- The appointment page is open ------------------------------

WebUI.selectOptionByValue(findTestObject('CURA/Page_CURA Healthcare Service/appointment/select_Tokyo CURA Healthcare Center'),
	'Hongkong CURA Healthcare Center', true)

WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/appointment/input_Apply for hospital readmission'))

WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/appointment/input_Medicaid_programs'))

// set the same day in the next week
def visitDate = LocalDateTime.now().plusWeeks(1)
def visitDateStr = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(visitDate)

WebUI.verifyElementPresent(findTestObject('CURA/Page_CURA Healthcare Service/appointment/input_visit_date'), 10)
WebUI.sendKeys(findTestObject('CURA/Page_CURA Healthcare Service/appointment/input_visit_date'), Keys.chord(Keys.ENTER))
WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/appointment/textarea_Comment_comment'), 'this is a comment')

// take the screenshot and the page source, save them into the store; using the AShot library
url = new URL(WebUI.getUrl())
takeScreenshotAndSavePageSourceUsingAShot(store, jobName, jobTimestamp, new Metadata([
	"profile": profile,          // "CURA_ProdcutionEnv" or "CURA_DevelopmentEnv"
	"URL": url.toString(),       // "https://katalon-demo-cura.herokuapp.com/#appointment"
	"URL.host": url.getHost(),   // "katalon-demo-cura.herokuapp.com"
	"URL.file": url.getFile(),   // "/#appointment"
	]))

WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/appointment/button_Book Appointment'))

// -------- the summary page is open ----------------------------------

// take the screenshot and the page source, save them into the store; using the AShot library
url = new URL(WebUI.getUrl())
takeScreenshotAndSavePageSourceUsingAShot(store, jobName, jobTimestamp, new Metadata([
	"profile": profile,          // "CURA_ProdcutionEnv" or "CURA_DevelopmentEnv"
	"URL": url.toString(),       // "https://katalon-demo-cura.herokuapp.com/appointment.php#summary"
	"URL.host": url.getHost(),   // "katalon-demo-cura.herokuapp.com"
	"URL.file": url.getFile(),   // "/appointment.php#summary"
	]))

WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/summary/a_Go to Homepage'))

// we are done
WebUI.closeBrowser()


/**
 * 
 * @param metadata
 * @return
 */
def takeScreenshotAndSavePageSourceUsingBuiltinKeyword(Store store, JobName jobName, JobTimestamp jobTimestamp, Metadata metadata) {
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

/**
 * 
 * @param store
 * @param jobName
 * @param jobTimestamp
 * @param metadata
 * @return
 */
def takeScreenshotAndSavePageSourceUsingAShot(Store store, JobName jobName, JobTimestamp jobTimestamp, Metadata metadata) {
	Objects.requireNonNull(store)
	Objects.requireNonNull(jobName)
	Objects.requireNonNull(jobTimestamp)
	Objects.requireNonNull(metadata)
	WebDriver driver = DriverFactory.getWebDriver()
	// take a screenshot using AShot library
	float devicePixelRatio = DevicePixelRatioResolver.resolveDPR(driver)
	BufferedImage bi = AShotWrapper.takeEntirePageImage(driver, 100, devicePixelRatio)
	Material image = store.write(jobName, jobTimestamp, FileType.PNG, metadata, bi)
	assert image != null
	// save the page source HTML
	String pageSource = driver.getPageSource()
	Material html = store.write(jobName, jobTimestamp, FileType.HTML, metadata, pageSource)
	assert html != null
}