package visualinspection

import com.kms.katalon.core.testobject.ConditionType
import com.kms.katalon.core.testobject.TestObject

class TestObjectFactory {
	
	static TestObject xpath(String xpath) {
		TestObject tObj = new TestObject(xpath)
		tObj.addProperty("xpath", ConditionType.EQUALS, xpath)
		return tObj
	}
}
