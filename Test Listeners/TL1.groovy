import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.annotation.BeforeTestSuite
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.context.TestSuiteContext

import internal.GlobalVariable

class TL1 {
	
	@BeforeTestSuite
	def beforeTestSuite(TestSuiteContext testSuiteContext) {
		GlobalVariable.CURRENT_TESTSUITE_NAME = testSuiteContext.getTestSuiteId().replace("Test Suites/", "").replaceAll("/", "_")
	}
	
	@BeforeTestCase
	def beforeTestCase(TestCaseContext testCaseContext) {
		GlobalVariable.CURRENT_TESTCASE_NAME = testCaseContext.getTestCaseId().replace("Test Cases/", "").replaceAll("/", "_")
	}

	
}