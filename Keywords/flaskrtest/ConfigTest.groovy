package flaskrtest

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
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

import org.openqa.selenium.WebDriver
import com.kms.katalon.core.annotation.AfterTestCase
import com.kms.katalon.core.annotation.AfterTestSuite
import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext

import groovy.json.*

public class ConfigTest {

	static String CONFIG_PATH = './Include/resources/config.json'
	static Integer DEFAULT_WAIT_TIME = 10
	static List<String> SUPPORTED_BROWSERS = ['chrome', 'firefox']

	static Map<String, Object> config() {
		JsonSlurper slurper = new JsonSlurper()
		return slurper.parse(new File(CONFIG_PATH))
	}

	static Map<String, Object> credential() {
		ZonedDateTime now = ZonedDateTime.now()
		String timestamp = DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(now)
		Map data = [
			'username': 'UN' + timestamp,
			'password': 'PW' + timestamp
		]
		return data
	}

	static String config_browser(Map<String, Object> config) {
		if (!config.containsKey('browser')) {
			throw new IllegalArgumentException("The config file does not contain 'browseer'")
		} else if (!SUPPORTED_BROWSERS.contains(config['browser'])) {
			throw new IllegalArgumentException("${config['browser']} is not a supported browser")
		}
		return config['browser']
	}

	/**
	 * validate and return the wait time from the config data
	 * @param confiig
	 * @return
	 */
	static Integer config_wait_time(Map<String, Object> config) {
		if (config.containsKey('wait_time')) {
			return config['wait_time']
		} else {
			DEFAULT_WAIT_TIME
		}
	}

	/*
	static WebDriver browser(config_browser, config_wait_time) {
		WebDriver driver
		if (config_browser == 'chrome') {
			KSWebDriverFactory factory = new KSWebDriverFactory.Builder(KSDriverTypeName.CHROME_DRIVER).build()
			driver = factory.newWebDriver()
		} else {
			throw new IllegalArgumentException("${config_browser} is not supported")
		}
		return driver
	}
	*/
}
