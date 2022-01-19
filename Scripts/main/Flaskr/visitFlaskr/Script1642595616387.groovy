import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import com.kazurayam.materialstore.Metadata
import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable
import visualinspection.TestObjectFactory

// check params which should be passed as the arguments of WebUI.callTestCases() call
Objects.requireNonNull(profile)
Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)

// check the GlobalVariables
assert GlobalVariable.URL != null, "GlobalVariable.URL is not defined"

WebUI.openBrowser('')
WebUI.setViewPortSize(1024,600)

WebUI.navigateToUrl("${GlobalVariable.URL}")

TestObject siteName = TestObjectFactory.xpath("//body/nav/h1[text()='Flaskr']")
WebUI.verifyElementPresent(siteName, 10)
URL url = new URL(WebUI.getUrl())
WebUI.callTestCase(findTestCase("main/common/takeScreenshotAndPageSource"),
	[
		"store": store,
		"jobName": jobName,
		"jobTimestamp": jobTimestamp,
		"metadata": Metadata.builderWithUrl(url)
			.put("profile", profile)
			.put("selector", "body")
			.build()
	]
)

WebUI.closeBrowser()