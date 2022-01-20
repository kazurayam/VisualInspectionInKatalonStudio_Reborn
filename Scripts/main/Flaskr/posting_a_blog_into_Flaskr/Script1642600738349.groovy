import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import com.kazurayam.ks.testobject.By
import com.kazurayam.ks.visualinspection.MaterializingContext
import com.kazurayam.materialstore.JobName
import com.kazurayam.materialstore.JobTimestamp
import com.kazurayam.materialstore.Metadata
import com.kazurayam.materialstore.Store
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

import internal.GlobalVariable

// check params which should be passed as the arguments of WebUI.callTestCases() call
Objects.requireNonNull(profile)
Objects.requireNonNull(store)
Objects.requireNonNull(jobName)
Objects.requireNonNull(jobTimestamp)

MaterializingContext matz = new MaterializingContext(profile, store, jobName, jobTimestamp)

String username = 'Alice'
String password = 'ThisIsNotAPassword'

// check the GlobalVariables
assert GlobalVariable.URL != null, "GlobalVariable.URL is not defined"

WebUI.openBrowser('')
WebUI.setViewPortSize(1024,600)

WebUI.navigateToUrl("${GlobalVariable.URL}")

TestObject siteName = By.xpath("//body/nav/h1[text()='Flaskr']")
WebUI.verifyElementPresent(siteName, 10)

Tuple materials = matz.materialize(new URL(WebUI.getUrl()))

WebUI.closeBrowser()
