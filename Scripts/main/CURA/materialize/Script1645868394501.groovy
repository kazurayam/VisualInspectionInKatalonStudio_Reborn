import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver

import com.kazurayam.materialstore.filesystem.Material
import com.kazurayam.materialstore.materialize.MaterializingPageFunctions
import com.kazurayam.materialstore.materialize.StorageDirectory
import com.kazurayam.materialstore.materialize.Target
import com.kazurayam.materialstore.materialize.Target.Builder
import com.kms.katalon.core.webui.driver.DriverFactory
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

/**
 * Test Cases/main/CURA/materialize
 */
Objects.requireNonNull(GlobalVariable.URL)
Objects.requireNonNull(GlobalVariable.Username)
Objects.requireNonNull(GlobalVariable.Password)

Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)

// -------- setup -----------------------------------------------------
WebUI.openBrowser('')
WebUI.setViewPortSize(1024, 800)

WebUI.navigateToUrl(GlobalVariable.URL)

WebDriver driver = DriverFactory.getWebDriver()
StorageDirectory sd = new StorageDirectory(store, jobName, jobTimestamp)



// -------- The top page is supposed to be open --------------------------------------
WebUI.verifyElementPresent(findTestObject('CURA/Page_CURA Healthcare Service/top/a_Make Appointment'), 5)

// take the screenshot and the page source, save them into the store; using the Katalon keyword
URL url = new URL(WebUI.getUrl())
Target target = Target.builder(url).put("step", "01").put("profile", "ProductionEnv").build()
Material screenshot1 = MaterializingPageFunctions.storeEntirePageScreenshot.accept(target, driver, sd)
Material html1 = MaterializingPageFunctions.storeHTMLSource.accept(target, driver, sd)

// we navigate to the next page (login)	
WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/top/a_Make Appointment'))



// -------- The login page is supposed to be open ------------------------------------
WebUI.verifyElementPresent(findTestObject('CURA/Page_CURA Healthcare Service/login/button_Login'), 5)

// type the Username
assert GlobalVariable.Username != null, "GlobalVariable.Username is not defined"
WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/login/input_Username_username'),
	GlobalVariable.Username)

// type the Password
assert GlobalVariable.Password != null, "GlobalVariable.Password is not defined"
WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/login/input_Password_password'),
	GlobalVariable.Password)

// we navigate to the next page (appointment)
WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/login/button_Login'))


// -------- The appointment page is supposed to be open ------------------------------
WebUI.verifyElementPresent(findTestObject('CURA/Page_CURA Healthcare Service/appointment/button_Book Appointment'), 5)

WebUI.selectOptionByValue(findTestObject('CURA/Page_CURA Healthcare Service/appointment/select_Tokyo CURA Healthcare Center'),
	'Hongkong CURA Healthcare Center', true)
WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/appointment/input_Apply for hospital readmission'))
WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/appointment/input_Medicaid_programs'))

// set the same day in the next week
def visitDate = LocalDateTime.now().plusWeeks(1)
def visitDateStr = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(visitDate)
WebUI.verifyElementPresent(findTestObject('CURA/Page_CURA Healthcare Service/appointment/input_visit_date'), 10)
WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/appointment/input_visit_date'), visitDateStr)
WebUI.sendKeys(findTestObject('CURA/Page_CURA Healthcare Service/appointment/input_visit_date'), Keys.chord(Keys.ENTER))
WebUI.setText(findTestObject('CURA/Page_CURA Healthcare Service/appointment/textarea_Comment_comment'), 'this is a comment')

// take the screenshot and the page source, save them into the store
url = new URL(WebUI.getUrl())
Target target2 = Target.builder(url).put("step", "02").put("profile", "ProductionEnv").build()
Material screenshot2 = MaterializingPageFunctions.storeEntirePageScreenshot.accept(target2, driver, sd)
Material html2 = MaterializingPageFunctions.storeHTMLSource.accept(target2, driver, sd)

// we navigate to the next page (summpary)
WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/appointment/button_Book Appointment'))



// -------- the summary page is supposed to be open ----------------------------------
WebUI.verifyElementPresent(findTestObject('CURA/Page_CURA Healthcare Service/summary/a_Go to Homepage'), 5)

// take the screenshot and the page source, save them into the store
url = new URL(WebUI.getUrl())
Target target3 = Target.builder(url).put("step", "03").put("profile", "ProductionEnv").build()
Material screenshot3 = MaterializingPageFunctions.storeEntirePageScreenshot.accept(target3, driver, sd)
Material html3 = MaterializingPageFunctions.storeHTMLSource.accept(target3, driver, sd)

// we navigate to the home page
WebUI.click(findTestObject('CURA/Page_CURA Healthcare Service/summary/a_Go to Homepage'))


// --------------------------------------------------------------------
// we are done
WebUI.closeBrowser()
