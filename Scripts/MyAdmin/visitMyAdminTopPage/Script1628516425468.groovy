import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import com.kazurayam.materialstore.MetadataImpl
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

// check params which should be passed as the arguments of WebUI.callTestCases() call
Objects.requireNonNull(profile)
Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)

// check the GlobalVariables
assert GlobalVariable.URL != null, "GlobalVariable.URL is not defined"

WebUI.openBrowser('')
WebUI.setViewPortSize(1024, 800)

WebUI.navigateToUrl("${GlobalVariable.URL}")

// wait for the <img> element appears in the page
String xpath = "//img[@alt='umineko']"
TestObject img = new TestObject(xpath)
img.addProperty("xpath", ConditionType.EQUALS, xpath)
WebUI.verifyElementPresent(img, 10)

// -------- The page is open -------------------------------------- 

// take the screenshot and the page source, save them into the store; using the Katalon keyword
URL url = new URL(WebUI.getUrl())

WebUI.callTestCase(findTestCase("common/takeScreenshotAndPageSource"), 
	[
		"store": store,
		"jobName": jobName,
		"jobTimestamp": jobTimestamp,
		"metadata": new MetadataImpl.Builder(url)
									.put("profile", profile)
									.put("selector", "body")
									.build()
	]
)

// we have done materializing the screenshot and the page source
WebUI.closeBrowser()


