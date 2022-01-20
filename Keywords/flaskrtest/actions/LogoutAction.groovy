package flaskrtest.actions

import static com.kms.katalon.core.checkpoint.CheckpointFactory.findCheckpoint
import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase
import static com.kms.katalon.core.testdata.TestDataFactory.findTestData
import static com.kms.katalon.core.testobject.ObjectRepository.findTestObject
import static com.kms.katalon.core.testobject.ObjectRepository.findWindowsObject

import com.kms.katalon.core.annotation.Keyword
import com.kms.katalon.core.checkpoint.Checkpoint
import com.kms.katalon.core.cucumber.keyword.CucumberBuiltinKeywords as CucumberKW
import com.kms.katalon.core.mobile.keyword.MobileBuiltInKeywords as Mobile
import com.kms.katalon.core.model.FailureHandling
import com.kms.katalon.core.testcase.TestCase
import com.kms.katalon.core.testdata.TestData
import com.kms.katalon.core.testobject.TestObject
import com.kms.katalon.core.webservice.keyword.WSBuiltInKeywords as WS
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI
import com.kms.katalon.core.windows.keyword.WindowsBuiltinKeywords as Windows

import internal.GlobalVariable

import org.openqa.selenium.WebDriver

import com.kms.katalon.core.util.KeywordUtil

import flaskrtest.pages.auth.LogInPage
import flaskrtest.pages.auth.RegisterCredentialPage
import flaskrtest.pages.blog.IndexPage
import flaskrtest.data.User


public class LogoutAction {

	static void do_logout(WebDriver browser, URL url) {

		Objects.requireNonNull(browser)
		Objects.requireNonNull(url)

		// now we go to the Index page
		IndexPage indexPage = new IndexPage(browser)
		indexPage.load(url)

		assert indexPage.logout_anchor_exists()

		indexPage.click_logout()

	}
}
