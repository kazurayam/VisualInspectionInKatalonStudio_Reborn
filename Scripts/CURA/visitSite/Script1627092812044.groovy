import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kazurayam.materialstore.store.JobName
import com.kazurayam.materialstore.store.JobTimestamp
import com.kazurayam.materialstore.store.Store
import com.kms.katalon.core.checkpoint.Checkpoint as Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling as FailureHandling
import com.kms.katalon.core.testcase.TestCase as TestCase
import com.kms.katalon.core.testdata.TestData as TestData
import com.kms.katalon.core.testng.keyword.TestNGBuiltinKeywords as TestNGKW
import com.kms.katalon.core.testobject.TestObject as TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows
import internal.GlobalVariable as GlobalVariable
import org.openqa.selenium.Keys as Keys
import org.openqa.selenium.WebDriver
import com.kms.katalon.core.webui.driver.DriverFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

// check params which should be passed as the arguments of WebUI.callTestCases() call
Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)

// check the GlobalVariables
assert GlobalVariable.Hostname != null, "GlobalVariable.Hostname is not defined"
assert GlobalVariable.Username != null, "GlobalVariable.Username is not defined"
assert GlobalVariable.Password != null, "GlobalVariable.Password is not defined"
assert GlobalVariable.SAVE_PAGE_SOURCE != null, "GlobalVariable.SAVE_PAGE_SORCE is not defined"

WebDriver driver = DriverFactory.getWebDriver()
assert driver != null, "could not get the WebDriver instance"
	
String url = "http://${GlobalVariable.Hostname}/"
WebUI.comment("going to visit url: " + url)

WebUI.navigateToUrl(url)
// we assume that Katalon Studio is configured to implicitly wait for the page completely loaded

// The top page is open 

WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/top/a_Make Appointment'))

// The login page is open

WebUI.verifyElementPresent(findTestObject('CURA/Page_CURA Healthcare Service/login/input_Username_username'), 10)

assert GlobalVariable.Username != null, "GlobalVariable.Username is not defined"

WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/login/input_Username_username'),
	GlobalVariable.Username)

assert GlobalVariable.Password != null, "GlobalVariable.Password is not defined"

WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/login/input_Password_password'),
	GlobalVariable.Password)

WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/login/button_Login'))

// The appointment page is open

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
WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/appointment/button_Book Appointment'))

// the summary page is open

WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/summary/a_Go to Homepage'))

// we are done

WebUI.closeBrowser()
