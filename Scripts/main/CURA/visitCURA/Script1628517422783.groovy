import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.openqa.selenium.Keys

import com.kazurayam.materialstore.metadata.Metadata
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable


Objects.requireNonNull(GlobalVariable.URL)
Objects.requireNonNull(GlobalVariable.Username)
Objects.requireNonNull(GlobalVariable.Password)

Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)

WebUI.openBrowser('')
WebUI.setViewPortSize(1024, 800)

WebUI.navigateToUrl(GlobalVariable.URL)
// we assume that Katalon Studio is configured to implicitly wait for the page completely loaded

// -------- The top page is open --------------------------------------

// take the screenshot and the page source, save them into the store; using the Katalon keyword
URL url = new URL(WebUI.getUrl())

WebUI.callTestCase(findTestCase("main/common/takeScreenshotAndPageSource"), [
	"store": store,
	"jobName": jobName,
	"jobTimestamp": jobTimestamp,
	"metadata": Metadata.builderWithUrl(url).build()
	])

WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/top/a_Make Appointment'))

// -------- The login page is open ------------------------------------

WebUI.verifyElementPresent(findTestObject('CURA/Page_CURA Healthcare Service/login/input_Username_username'), 10)

assert GlobalVariable.Username != null, "GlobalVariable.Username is not defined"

WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/login/input_Username_username'),
	GlobalVariable.Username)

assert GlobalVariable.Password != null, "GlobalVariable.Password is not defined"

WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/login/input_Password_password'),
	GlobalVariable.Password)
	
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
WebUI.callTestCase(findTestCase("main/common/takeScreenshotAndPageSource"),[
	"store": store,
	"jobName": jobName,
	"jobTimestamp": jobTimestamp,
	"metadata": Metadata.builderWithUrl(url).build()
	])

WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/appointment/button_Book Appointment'))

// -------- the summary page is open ----------------------------------

// take the screenshot and the page source, save them into the store; using the AShot library
url = new URL(WebUI.getUrl())
WebUI.callTestCase(findTestCase("main/common/takeScreenshotAndPageSource"),[
	"store": store,
	"jobName": jobName,
	"jobTimestamp": jobTimestamp,
	"metadata": Metadata.builderWithUrl(url).build()
	])
	
WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/summary/a_Go to Homepage'))

// we are done
WebUI.closeBrowser()
