import static com.kms.katalon.core.testcase.TestCaseFactory.findTestCase

import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

Objects.requireNonNull(store)

return WebUI.callTestCase(findTestCase("Test Cases/main/common/createIndex"), ["store":store])
