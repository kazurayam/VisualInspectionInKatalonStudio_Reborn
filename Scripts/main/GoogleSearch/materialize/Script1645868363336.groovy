import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.testobject.ConditionType


String urlStr = "https://www.google.com/"

Path jobDir = Paths.get(System.getProperty('user.home')).resolve('Documents').resolve('Automation').resolve('Test Practice')
Date today = new Date()
Path timestampDir = jobDir.resolve("${today.format('MM_dd_yy')}_${today.format('hh_mm_ss')}")
Files.createDirectories(timestampDir)

WebUI.openBrowser('')
WebUI.navigateToUrl(urlStr)
WebUI.waitForPageLoad(10)

Integer seq = 0
Path png1 = timestampDir.resolve("screenshot_${seq}.png") 
WebUI.takeScreenshot(png1.toFile().toString())

String locator = "//input[@name='q']"
TestObject inputQ = new TestObject(locator)
inputQ.addProperty("xpath", ConditionType.EQUALS, locator)

WebUI.waitForElementPresent(inputQ, 10)
WebUI.setText(inputQ, "katalon")

seq = 1
Path png2 = timestampDir.resolve("screenshot_${seq}.png")
WebUI.takeScreenshot(png2.toFile().toString())

WebUI.closeBrowser()