import com.kazurayam.materialstore.dot.MPGVisualizer
import com.kazurayam.materialstore.filesystem.JobTimestamp
import com.kazurayam.materialstore.filesystem.MaterialstoreException
import com.kazurayam.materialstore.inspector.Inspector
import com.kazurayam.materialstore.reduce.MaterialProductGroup
import com.kazurayam.materialstore.reduce.Reducer
import com.kms.katalon.core.util.KeywordUtil
import com.kms.katalon.core.webui.keyword.WebUiBuiltInKeywords as WebUI

//import java.util.function.BiFunction

/**
 * Test Cases/main/CURA/reduce
 */

assert store != null
assert currentMaterialList != null
assert sortKeys != null

WebUI.comment("store=${store}")
WebUI.comment("currentMaterialList=${currentMaterialList}")
WebUI.comment("sortKeys=${sortKeys}")

MaterialProductGroup inspected = null

try {
	MaterialProductGroup reduced = Reducer.chronos(store, currentMaterialList)

	Inspector inspector = Inspector.newInstance(store)
	inspector.setSortKeys(sortKeys)
	inspected = inspector.reduceAndSort(reduced)

	if (inspected.getNumberOfBachelors() > 0) {
		// if any bachelor found, generate diagram of MProductGroup object
		MPGVisualizer visualizer = new MPGVisualizer(store)
		visualizer.visualize(inspected.getJobName(), JobTimestamp.now(), inspected);
	}
} catch (MaterialstoreException ex) {
	KeywordUtil.markWarning(ex.getMessage())
}

return inspected
